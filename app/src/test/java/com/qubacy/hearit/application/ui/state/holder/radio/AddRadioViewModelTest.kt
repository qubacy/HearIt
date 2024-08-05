package com.qubacy.hearit.application.ui.state.holder.radio

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application._common.exception.HearItException
import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.RadioDomainSketch
import com.qubacy.hearit.application.domain.usecase.radio.add._common.AddRadioUseCase
import com.qubacy.hearit.application.ui._common.presentation.mapper._common.RadioPresentationDomainModelMapper
import com.qubacy.hearit.application.ui.state.holder.radio.validator._common.RadioInputWrapperValidator
import com.qubacy.hearit.application.ui.state.state.radio.AddRadioState
import com.qubacy.hearit.application.ui.state.holder.radio.wrapper.RadioInputWrapper
import com.qubacy.hearit.application.ui.state.holder.radio.wrapper.mapper._common.RadioInputWrapperRadioDomainSketchMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any

class AddRadioViewModelTest {
  @get:Rule
  val instantExecutionRule = InstantTaskExecutorRule()

  private val _coroutineDispatcher = StandardTestDispatcher()

  private lateinit var _useCaseMock: AddRadioUseCase
  private lateinit var _radioInputValidatorMock: RadioInputWrapperValidator
  private lateinit var _radioInputWrapperDomainSketchMapperMock: RadioInputWrapperRadioDomainSketchMapper

  private lateinit var _instance: AddRadioViewModel

  @Before
  fun setup() {
    _useCaseMock = mockUseCase()
    _radioInputValidatorMock = mockRadioInputValidator()
    _radioInputWrapperDomainSketchMapperMock = mockRadioInputWrapperDomainSketchMapper()

    _instance = AddRadioViewModel(
      _coroutineDispatcher, _useCaseMock, _radioInputValidatorMock,
      _radioInputWrapperDomainSketchMapperMock
    )
  }

  private fun mockUseCase(): AddRadioUseCase {
    return Mockito.mock(AddRadioUseCase::class.java)
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
  fun addRadioTest() = runTest(_coroutineDispatcher) {
    val radioId = 0L

    val radioInputWrapper = RadioInputWrapper("", url = "")
    val radioDomainSketch = RadioDomainSketch("", url = "")

    val expectedInitState = AddRadioState(isLoading = true)
    val expectedFinalState = AddRadioState(addedRadioId = radioId)

    Mockito.`when`(_radioInputValidatorMock.validate(any())).thenReturn(true)
    Mockito.`when`(_radioInputWrapperDomainSketchMapperMock.map(any())).thenReturn(radioDomainSketch)
    Mockito.`when`(_useCaseMock.addRadio(any())).thenReturn(radioId)

    _instance.addRadio(radioInputWrapper)

    val gottenInitState = _instance.state.value

    advanceUntilIdle()

    val gottenFinalState = _instance.state.value

    Mockito.verify(_radioInputValidatorMock).validate(any())
    Mockito.verify(_radioInputWrapperDomainSketchMapperMock).map(any())
    Mockito.verify(_useCaseMock).addRadio(any())

    Assert.assertEquals(expectedInitState, gottenInitState)
    Assert.assertEquals(expectedFinalState, gottenFinalState)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun stateChangedToErrorOnUseCaseAddRadioFlowCaughtExceptionTest() = runTest(
    _coroutineDispatcher
  ) {
    val radioInputWrapper = RadioInputWrapper("", url = "")
    val errorReference = ErrorReference(0)

    val expectedState = AddRadioState(error = errorReference)

    Mockito.`when`(_useCaseMock.addRadio(any())).thenAnswer { throw HearItException(errorReference) }

    _instance.addRadio(radioInputWrapper)
    advanceUntilIdle()

    val gottenState = _instance.state.value

    Assert.assertEquals(expectedState, gottenState)
  }
}