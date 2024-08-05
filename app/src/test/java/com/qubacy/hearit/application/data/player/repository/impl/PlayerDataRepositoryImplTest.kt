package com.qubacy.hearit.application.data.player.repository.impl

import com.qubacy.hearit.application.data.player.model.PlayerInfoDataModel
import com.qubacy.hearit.application.data.player.model.mapper._common.PlayerInfoDataModelDataStoreModelMapper
import com.qubacy.hearit.application.data.player.repository._common.source.local._common.PlayerDataStoreDataSource
import com.qubacy.hearit.application.data.player.repository._common.source.local._common.model.PlayerInfoDataStoreModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any

class PlayerDataRepositoryImplTest {
  private lateinit var _localSourceMock: PlayerDataStoreDataSource
  private lateinit var _mapperMock: PlayerInfoDataModelDataStoreModelMapper

  private val _dispatcher = StandardTestDispatcher()

  @Before
  fun setup() {
    _localSourceMock = mockLocalSource()
    _mapperMock = mockMapper()
  }

  private fun mockLocalSource(): PlayerDataStoreDataSource {
    return Mockito.mock(PlayerDataStoreDataSource::class.java)
  }

  private fun mockMapper(): PlayerInfoDataModelDataStoreModelMapper {
    return Mockito.mock(PlayerInfoDataModelDataStoreModelMapper::class.java)
  }

  @Test
  fun getPlayerInfoTest() = runTest(_dispatcher) {
    val playerInfoDataStoreModel = PlayerInfoDataStoreModel()
    val expectedPlayerInfoDataModel = PlayerInfoDataModel()

    Mockito.`when`(_mapperMock.map(any<PlayerInfoDataStoreModel>()))
      .thenReturn(expectedPlayerInfoDataModel)
    Mockito.`when`(_localSourceMock.getPlayerInfo()).thenReturn(flowOf(playerInfoDataStoreModel))

    val playerDataRepository = PlayerDataRepositoryImpl(_localSourceMock, _mapperMock)

    val gottenPlayerInfoDataModel = playerDataRepository.getPlayerInfo().first()

    Mockito.verify(_mapperMock).map(any<PlayerInfoDataStoreModel>())
    Mockito.verify(_localSourceMock).getPlayerInfo()

    Assert.assertEquals(expectedPlayerInfoDataModel, gottenPlayerInfoDataModel)
  }

  @Test
  fun updatePlayerInfoTest() = runTest(_dispatcher) {
    val playerInfoDataModel = PlayerInfoDataModel()
    val playerInfoDataStoreModel = PlayerInfoDataStoreModel()

    Mockito.`when`(_mapperMock.map(any<PlayerInfoDataModel>())).thenReturn(playerInfoDataStoreModel)

    val playerDataRepository = PlayerDataRepositoryImpl(_localSourceMock, _mapperMock)

    playerDataRepository.updatePlayerInfo(playerInfoDataModel)

    Mockito.verify(_mapperMock).map(any<PlayerInfoDataModel>())
    Mockito.verify(_localSourceMock).updatePlayerInfo(any())
  }
}