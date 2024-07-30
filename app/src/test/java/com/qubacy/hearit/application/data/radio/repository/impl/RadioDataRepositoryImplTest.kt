package com.qubacy.hearit.application.data.radio.repository.impl

import com.qubacy.hearit.application.data.radio.model.RadioDataModel
import com.qubacy.hearit.application.data.radio.model.mapper._common.RadioDataModelLocalModelMapper
import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.RadioLocalDatabaseSource
import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.RadioLocalModel
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

class RadioDataRepositoryImplTest {
  private lateinit var _localSourceMock: RadioLocalDatabaseSource
  private lateinit var _mapperMock: RadioDataModelLocalModelMapper

  private lateinit var _instance: RadioDataRepositoryImpl

  private val _coroutineDispatcher = StandardTestDispatcher()

  @Before
  fun setup() {
    _localSourceMock = mockLocalSource()
    _mapperMock = mockMapper()

    _instance = RadioDataRepositoryImpl(_localSourceMock, _mapperMock)
  }

  private fun mockLocalSource(): RadioLocalDatabaseSource {
    return Mockito.mock(RadioLocalDatabaseSource::class.java)
  }

  private fun mockMapper(): RadioDataModelLocalModelMapper {
    return Mockito.mock(RadioDataModelLocalModelMapper::class.java)
  }

  @Test
  fun getAllRadiosTest() = runTest(_coroutineDispatcher) {
    val radioLocalModel = RadioLocalModel(0, "", null, null, "")
    val radioDataModel = RadioDataModel(0, "", null, null, "")

    val radioLocalModelList = listOf(radioLocalModel)
    val expectedRadioDataModelList = listOf(radioDataModel)

    lateinit var gottenRadioDataModelList: List<RadioDataModel>

    Mockito.`when`(_mapperMock.map(any<RadioLocalModel>())).thenReturn(radioDataModel)
    Mockito.`when`(_localSourceMock.getAllRadios()).thenReturn(flowOf(radioLocalModelList))

    _instance.getAllRadios().collect {
      gottenRadioDataModelList = it
    }

    Mockito.verify(_mapperMock).map(any<RadioLocalModel>())
    Mockito.verify(_localSourceMock).getAllRadios()

    Assert.assertArrayEquals(
      expectedRadioDataModelList.toTypedArray(),
      gottenRadioDataModelList.toTypedArray()
    )
  }

  @Test
  fun getAllRadiosThrowsExceptionTest() = runTest(_coroutineDispatcher) {
    val expectedException = IllegalStateException()

    lateinit var gottenException: Throwable

    Mockito.`when`(_localSourceMock.getAllRadios()).thenReturn(flow {
      throw expectedException
    })

    _instance.getAllRadios().catch { gottenException = it }.collect { }

    Mockito.verify(_localSourceMock).getAllRadios()

    Assert.assertEquals(expectedException, gottenException)
  }

  @Test
  fun addRadioTest() = runTest(_coroutineDispatcher) {
    val expectedRadioId = 0L

    val radioLocalModel = RadioLocalModel(null, "", null, null, "")
    val radioDataModel = RadioDataModel(null, "", null, null, "")

    Mockito.`when`(_mapperMock.map(any<RadioDataModel>())).thenReturn(radioLocalModel)
    Mockito.`when`(_localSourceMock.addRadio(any())).thenReturn(expectedRadioId)

    val gottenRadioId = _instance.addRadio(radioDataModel)

    Mockito.verify(_mapperMock).map(any<RadioDataModel>())
    Mockito.verify(_localSourceMock).addRadio(any())

    Assert.assertEquals(expectedRadioId, gottenRadioId)
  }

  @Test
  fun updateRadioTest() = runTest(_coroutineDispatcher) {
    val radioLocalModel = RadioLocalModel(0, "", null, null, "")
    val radioDataModel = RadioDataModel(0, "", null, null, "")

    Mockito.`when`(_mapperMock.map(any<RadioDataModel>())).thenReturn(radioLocalModel)

    _instance.updateRadio(radioDataModel)

    Mockito.verify(_mapperMock).map(any<RadioDataModel>())
    Mockito.verify(_localSourceMock).updateRadio(any())
  }
}