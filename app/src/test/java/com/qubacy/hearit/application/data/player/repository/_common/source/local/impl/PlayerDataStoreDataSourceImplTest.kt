package com.qubacy.hearit.application.data.player.repository._common.source.local.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.qubacy.hearit.application.data.player.repository._common.source.local._common.model.PlayerInfoDataStoreModel
import com.qubacy.hearit.application.data.player.repository._common.source.local._common.model.mapper._common.PlayerInfoDataStorePreferenceMapper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any

class PlayerDataStoreDataSourceImplTest {
  private lateinit var _dataStoreMock: DataStore<Preferences>
  private lateinit var _mapperMock: PlayerInfoDataStorePreferenceMapper

  private val _dispatcher = StandardTestDispatcher()

  @Before
  fun setup() {
    _dataStoreMock = mockDataStore()
    _mapperMock = mockMapper()
  }

  private fun mockDataStore(): DataStore<Preferences> {
    return Mockito.mock(DataStore::class.java) as DataStore<Preferences>
  }

  private fun mockMapper(): PlayerInfoDataStorePreferenceMapper {
    return Mockito.mock(PlayerInfoDataStorePreferenceMapper::class.java)
  }

  @Test
  fun getPlayerInfoTest() = runTest(_dispatcher) {
    val preferencesFlow = flowOf(Mockito.mock(Preferences::class.java))
    val expectedPlayerInfoDataStoreModel = PlayerInfoDataStoreModel()

    Mockito.`when`(_mapperMock.map(any())).thenReturn(expectedPlayerInfoDataStoreModel)
    Mockito.`when`(_dataStoreMock.data).thenReturn(preferencesFlow)

    val playerInfoDataStoreDataSource = PlayerDataStoreDataSourceImpl(_dataStoreMock, _mapperMock)

    val gottenPlayerInfoDataStoreModel = playerInfoDataStoreDataSource.getPlayerInfo().first()

    Mockito.verify(_mapperMock).map(any())
    Mockito.verify(_dataStoreMock).data

    Assert.assertEquals(expectedPlayerInfoDataStoreModel, gottenPlayerInfoDataStoreModel)
  }

  @Test
  fun updatePlayerInfoTest() = runTest(_dispatcher) {
    val playerInfoDataStoreModel = PlayerInfoDataStoreModel()

    val playerInfoDataStoreDataSource = PlayerDataStoreDataSourceImpl(_dataStoreMock, _mapperMock)

    playerInfoDataStoreDataSource.updatePlayerInfo(playerInfoDataStoreModel)

    Mockito.verify(_dataStoreMock).edit(any())
  }
}