package com.qubacy.hearit.application.data.radio.repository._common.source.local.impl

import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.dao.RadioLocalDatabaseSourceDao
import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.dao.entity.RadioDatabaseEntity
import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.RadioLocalModel
import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.mapper._common.RadioDatabaseEntityLocalModelMapper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any

class RadioLocalDatabaseSourceImplTest {
  private lateinit var _daoMock: RadioLocalDatabaseSourceDao
  private lateinit var _mapperMock: RadioDatabaseEntityLocalModelMapper

  private lateinit var _instance: RadioLocalDatabaseSourceImpl

  private val _coroutineDispatcher = StandardTestDispatcher()

  @Before
  fun setup() {
    _daoMock = mockRadioLocalDatabaseSourceDao()
    _mapperMock = mockRadioDatabaseEntityLocalModelMapper()

    _instance = RadioLocalDatabaseSourceImpl(_daoMock, _mapperMock)
  }

  private fun mockRadioLocalDatabaseSourceDao(): RadioLocalDatabaseSourceDao {
    return Mockito.mock(RadioLocalDatabaseSourceDao::class.java)
  }

  private fun mockRadioDatabaseEntityLocalModelMapper(): RadioDatabaseEntityLocalModelMapper {
    return Mockito.mock(RadioDatabaseEntityLocalModelMapper::class.java)
  }

  @Test
  fun getAllRadiosTest() = runTest(_coroutineDispatcher) {
    val radioDatabaseEntity = RadioDatabaseEntity(null, "", null, null, "")
    val radioLocalModel = RadioLocalModel(null, "", null, null, "")

    val radioDatabaseEntityList = listOf(radioDatabaseEntity)
    val expectedRadioLocalModelList = listOf(radioLocalModel)

    Mockito.`when`(_daoMock.allRadios()).thenReturn(flowOf(radioDatabaseEntityList))
    Mockito.`when`(_mapperMock.map(any<RadioDatabaseEntity>())).thenReturn(radioLocalModel)

    lateinit var gottenRadioLocalModelList: List<RadioLocalModel>

    _instance.getAllRadios().collect {
      gottenRadioLocalModelList = it
    }

    Mockito.verify(_daoMock).allRadios()
    Mockito.verify(_mapperMock).map(any<RadioDatabaseEntity>())

    Assert.assertArrayEquals(
      expectedRadioLocalModelList.toTypedArray(),
      gottenRadioLocalModelList.toTypedArray()
    )
  }

  @Test
  fun getAllRadiosThrowsExceptionTest() = runTest(_coroutineDispatcher) {
    val expectedException = IllegalStateException()

    Mockito.`when`(_daoMock.allRadios()).thenReturn(flow {
      throw expectedException
    })

    lateinit var gottenException: Throwable

    _instance.getAllRadios().catch { gottenException = it }.collect {}

    Mockito.verify(_daoMock).allRadios()

    Assert.assertEquals(expectedException, gottenException)
  }

  @Test
  fun addRadioTest() = runTest(_coroutineDispatcher) {
    val expectedRadioId = 0L

    val radioDatabaseEntity = RadioDatabaseEntity(expectedRadioId, "", null, null, "")
    val radioLocalModel = RadioLocalModel(expectedRadioId, "", null, null, "")

    Mockito.`when`(_mapperMock.map(any<RadioLocalModel>())).thenReturn(radioDatabaseEntity)
    Mockito.`when`(_daoMock.insertRadio(any())).thenReturn(expectedRadioId)

    val gottenRadioId = _instance.addRadio(radioLocalModel)

    Mockito.verify(_mapperMock).map(any<RadioLocalModel>())
    Mockito.verify(_daoMock).insertRadio(any())

    Assert.assertEquals(expectedRadioId, gottenRadioId)
  }

  @Test
  fun updateRadioTest() = runTest(_coroutineDispatcher) {
    val radioId = 0L

    val radioDatabaseEntity = RadioDatabaseEntity(radioId, "", null, null, "")
    val radioLocalModel = RadioLocalModel(radioId, "", null, null, "")

    Mockito.`when`(_mapperMock.map(any<RadioLocalModel>())).thenReturn(radioDatabaseEntity)

    _instance.updateRadio(radioLocalModel)

    Mockito.verify(_mapperMock).map(any<RadioLocalModel>())
    Mockito.verify(_daoMock).updateRadio(any())
  }
}