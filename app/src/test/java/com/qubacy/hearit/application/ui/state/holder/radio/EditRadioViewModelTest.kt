package com.qubacy.hearit.application.ui.state.holder.radio

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application._common.exception.HearItException
import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.RadioDomainSketch
import com.qubacy.hearit.application.domain.usecase.radio.edit._common.EditRadioUseCase
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui._common.presentation.mapper.domain._common.RadioPresentationDomainModelMapper
import com.qubacy.hearit.application.ui.state.holder.radio.validator._common.RadioInputWrapperValidator
import com.qubacy.hearit.application.ui.state.holder.radio.wrapper.RadioInputWrapper
import com.qubacy.hearit.application.ui.state.holder.radio.wrapper.mapper._common.RadioInputWrapperRadioDomainSketchMapper
import com.qubacy.hearit.application.ui.state.state.radio.EditRadioState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any

class EditRadioViewModelTest {
  @get:Rule
  val instantExecutionRule = InstantTaskExecutorRule()

  private val _coroutineDispatcher = StandardTestDispatcher()

  private lateinit var _savedStateHandleMock: SavedStateHandle
  private lateinit var _useCaseMock: EditRadioUseCase
  private lateinit var _radioInputValidatorMock: RadioInputWrapperValidator
  private lateinit var _radioDomainModelPresentationMapperMock: RadioPresentationDomainModelMapper
  private lateinit var _radioInputWrapperDomainSketchMapperMock: RadioInputWrapperRadioDomainSketchMapper

  private lateinit var _instance: EditRadioViewModel

  @Before
  fun setup() {
    _savedStateHandleMock = mockSavedStateHandle()
    _useCaseMock = mockUseCase()
    _radioInputValidatorMock = mockRadioInputValidator()
    _radioDomainModelPresentationMapperMock = mockRadioDomainModelPresentationMapper()
    _radioInputWrapperDomainSketchMapperMock = mockRadioInputWrapperDomainSketchMapper()

    _instance = EditRadioViewModel(
      _savedStateHandleMock,
      _coroutineDispatcher, _useCaseMock, _radioInputValidatorMock,
      _radioDomainModelPresentationMapperMock, _radioInputWrapperDomainSketchMapperMock
    )
  }

  private fun mockSavedStateHandle(): SavedStateHandle {
    return Mockito.mock(SavedStateHandle::class.java)
  }

  private fun mockUseCase(): EditRadioUseCase {
    return Mockito.mock(EditRadioUseCase::class.java)
  }

  private fun mockRadioInputValidator(): RadioInputWrapperValidator {
    return Mockito.mock(RadioInputWrapperValidator::class.java)
  }

  private fun mockRadioDomainModelPresentationMapper(): RadioPresentationDomainModelMapper {
    return Mockito.mock(RadioPresentationDomainModelMapper::class.java)
  }

  private fun mockRadioInputWrapperDomainSketchMapper(): RadioInputWrapperRadioDomainSketchMapper {
    return Mockito.mock(RadioInputWrapperRadioDomainSketchMapper::class.java)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun observeRadioTest() = runTest(_coroutineDispatcher) {
    val radioId = 0L
    val radioDomainModel = RadioDomainModel(radioId, "", url = "")
    val radioPresentation = RadioPresentation(radioId, "", url = "")

    val expectedInitState = EditRadioState(isLoading = true)
    val expectedFinalState = EditRadioState(loadedRadio = radioPresentation)

    Mockito.`when`(_savedStateHandleMock.get<Long>(any())).thenReturn(radioId)
    Mockito.`when`(_radioDomainModelPresentationMapperMock.map(any())).thenReturn(radioPresentation)
    Mockito.`when`(_useCaseMock.getRadio(any())).thenReturn(flowOf(radioDomainModel))

    _instance.observeRadio()

    val gottenInitState = _instance.state.value

    advanceUntilIdle()

    val gottenFinalState = _instance.state.value

    Mockito.verify(_savedStateHandleMock).get<Long>(any())
    Mockito.verify(_radioDomainModelPresentationMapperMock).map(any())
    Mockito.verify(_useCaseMock).getRadio(any())

    Assert.assertEquals(expectedInitState, gottenInitState)
    Assert.assertEquals(expectedFinalState, gottenFinalState)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun stateChangedToErrorOnUseCaseGetRadioFlowCaughtExceptionTest() = runTest(
    _coroutineDispatcher
  ) {
    val radioId = 0L
    val errorReference = ErrorReference(0)

    val expectedInitState = EditRadioState(isLoading = true)
    val expectedFinalState = EditRadioState(error = errorReference)

    Mockito.`when`(_savedStateHandleMock.get<Long>(any())).thenReturn(radioId)
    Mockito.`when`(_useCaseMock.getRadio(any())).thenReturn(
      flow {
        throw HearItException(errorReference)
      }
    )

    _instance.observeRadio()

    val gottenInitState = _instance.state.value

    advanceUntilIdle()

    val gottenFinalState = _instance.state.value

    Mockito.verify(_savedStateHandleMock).get<Long>(any())
    Mockito.verify(_useCaseMock).getRadio(any())

    Assert.assertEquals(expectedInitState, gottenInitState)
    Assert.assertEquals(expectedFinalState, gottenFinalState)
  }

  @Test
  fun stopObservingRadioTest() = runTest(_coroutineDispatcher) {
    val getRadioJobFieldReflection = EditRadioViewModel::class.java
      .getDeclaredField("_getRadioJob").apply { isAccessible = true }

    Mockito.`when`(_savedStateHandleMock.get<Long>(any())).thenReturn(0)
    Mockito.`when`(_useCaseMock.getRadio(any())).thenReturn(flowOf())

    _instance.observeRadio()
    _instance.stopObservingRadio()

    val gottenGetRadioJob = getRadioJobFieldReflection.get(_instance)

    Assert.assertNull(gottenGetRadioJob)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun saveRadioTest() = runTest(_coroutineDispatcher) {
    val radioId = 0L

    val radioDomainSketch = RadioDomainSketch("", url = "")
    val radioInputWrapper = RadioInputWrapper("", url = "")

    val radioPresentation = RadioPresentation(radioId, "", url = "")

    val stateLiveData = getStateLiveData()

    val expectedInitState = EditRadioState(isLoading = true, loadedRadio = radioPresentation)
    val expectedFinalState = EditRadioState(
      isLoading = false, savedRadioId = radioId, loadedRadio = radioPresentation
    )

    Mockito.`when`(_radioInputValidatorMock.validate(any())).thenReturn(true)
    Mockito.`when`(_radioInputWrapperDomainSketchMapperMock.map(any())).thenReturn(radioDomainSketch)

    stateLiveData.value = EditRadioState(loadedRadio = radioPresentation)

    _instance.saveRadio(radioInputWrapper)

    val gottenInitState = _instance.state.value

    advanceUntilIdle()

    val gottenFinalState = _instance.state.value

    Mockito.verify(_radioInputValidatorMock).validate(any())
    Mockito.verify(_radioInputWrapperDomainSketchMapperMock).map(any())

    Assert.assertEquals(expectedInitState, gottenInitState)
    Assert.assertEquals(expectedFinalState, gottenFinalState)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun stateChangedToErrorOnUseCaseSaveRadioFlowCaughtExceptionTest() = runTest(
    _coroutineDispatcher
  ) {
    val radioDomainSketch = RadioDomainSketch("", url = "")
    val radioInputWrapper = RadioInputWrapper("", url = "")

    val radioPresentation = RadioPresentation(0, "", url = "")
    val errorReference = ErrorReference(0)

    val stateLiveData = getStateLiveData()

    val expectedInitState = EditRadioState(isLoading = true, loadedRadio = radioPresentation)
    val expectedFinalState = EditRadioState(
      isLoading = false, loadedRadio = radioPresentation, error = errorReference)

    Mockito.`when`(_radioInputValidatorMock.validate(any())).thenReturn(true)
    Mockito.`when`(_radioInputWrapperDomainSketchMapperMock.map(any())).thenReturn(radioDomainSketch)
    Mockito.`when`(_useCaseMock.saveRadio(Mockito.anyLong(), any()))
      .thenAnswer { throw HearItException(errorReference) }

    stateLiveData.value = EditRadioState(loadedRadio = radioPresentation)

    _instance.saveRadio(radioInputWrapper)

    val gottenInitState = _instance.state.value

    advanceUntilIdle()

    val gottenFinalState = _instance.state.value

    Mockito.verify(_radioInputValidatorMock).validate(any())
    Mockito.verify(_radioInputWrapperDomainSketchMapperMock).map(any())

    Assert.assertEquals(expectedInitState, gottenInitState)
    Assert.assertEquals(expectedFinalState, gottenFinalState)
  }

  private fun getStateLiveData(): MutableLiveData<EditRadioState> {
    return EditRadioViewModel::class.java.getDeclaredField("_state")
      .apply { isAccessible = true }.get(_instance) as MutableLiveData<EditRadioState>
  }
}