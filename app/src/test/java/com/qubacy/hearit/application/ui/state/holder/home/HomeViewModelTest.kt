package com.qubacy.hearit.application.ui.state.holder.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application._common.exception.HearItException
import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.domain.usecase.home._common.HomeUseCase
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui._common.presentation.mapper._common.RadioDomainModelRadioPresentationMapper
import com.qubacy.hearit.application.ui.state.state.HomeState
import com.qubacy.hearit.application.ui.state.state.PlayerState
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
import java.lang.reflect.Field

class HomeViewModelTest {
  @get:Rule
  val instantExecutionRule = InstantTaskExecutorRule()

  private val _coroutineDispatcher = StandardTestDispatcher()

  private lateinit var _useCaseMock: HomeUseCase
  private lateinit var _radioMapperMock: RadioDomainModelRadioPresentationMapper

  private lateinit var _instance: HomeViewModel

  @Before
  fun setup() {
    _useCaseMock = mockUseCase()
    _radioMapperMock = mockRadioMapper()

    _instance = HomeViewModel(_coroutineDispatcher, _useCaseMock, _radioMapperMock)
  }

  private fun mockUseCase(): HomeUseCase {
    return Mockito.mock(HomeUseCase::class.java)
  }

  private fun mockRadioMapper(): RadioDomainModelRadioPresentationMapper {
    return Mockito.mock(RadioDomainModelRadioPresentationMapper::class.java)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun observeRadioListCallLeadsToCallingUseCaseMethodAndLoadingStateTest() = runTest(
    _coroutineDispatcher
  ) {
    val expectedState = HomeState(isLoading = true)

    Mockito.`when`(_useCaseMock.getRadioList()).thenReturn(flow {  })

    _instance.observeRadioList()
    advanceUntilIdle()

    Mockito.verify(_useCaseMock).getRadioList()

    val gottenState = _instance.state.value

    Assert.assertEquals(expectedState, gottenState)
  }

  @Test
  fun stopObservingRadioListCallLeadsToDisposingGetRadioJobTest() {
    val getRadioListJobFieldReflection = getGetRadioListJobFieldReflection()

    _instance.observeRadioList()

    val gottenInitGetRadioListJob = getRadioListJobFieldReflection.get(_instance)

    Assert.assertNotNull(gottenInitGetRadioListJob)

    _instance.stopObservingRadioList()

    val gottenFinalGetRadioListJob = getRadioListJobFieldReflection.get(_instance)

    Assert.assertNull(gottenFinalGetRadioListJob)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun stateChangedToSuccessOnDomainDataGottenTest() = runTest(
    _coroutineDispatcher
  ) {
    val radioDomainModel = RadioDomainModel(0, "", url = "")
    val expectedRadioPresentation = RadioPresentation(0, "", url = "")

    val expectedState = HomeState(radioList = listOf(expectedRadioPresentation))

    Mockito.`when`(_radioMapperMock.map(any())).thenReturn(expectedRadioPresentation)
    Mockito.`when`(_useCaseMock.getRadioList()).thenReturn(
      flowOf(listOf(radioDomainModel))
    )

    _instance.observeRadioList()
    advanceUntilIdle()

    Mockito.verify(_radioMapperMock).map(any())

    val gottenState = _instance.state.value

    Assert.assertEquals(expectedState, gottenState)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun stateChangedToErrorOnUseCaseGetRadioListFlowCaughtExceptionTest() = runTest(
    _coroutineDispatcher
  ) {
    val errorReference = ErrorReference(0)

    val expectedState = HomeState(error = errorReference)

    Mockito.`when`(_useCaseMock.getRadioList()).thenReturn(
      flow {
        throw HearItException(errorReference)
      }
    )

    _instance.observeRadioList()
    advanceUntilIdle()

    val gottenState = _instance.state.value

    Assert.assertEquals(expectedState, gottenState)
  }

  @Test
  fun setCurrentRadioTest() {
    val stateReference = getState(_instance)

    val radioId = 0L
    val radioPresentation = RadioPresentation(radioId, "", url = "")

    val initState = HomeState(radioList = listOf(radioPresentation))

    val expectedInitState = initState.copy(
      playerState = PlayerState(
        currentRadio = radioPresentation,
        isRadioPlaying = true
      ),
      isLoading = true
    )
    val expectedFinalState = expectedInitState.copy(isLoading = false)

    stateReference.value = initState

    _instance.setCurrentRadio(radioId)

    val gottenInitState = _instance.state.value

    Assert.assertEquals(expectedInitState, gottenInitState)

    // todo: finish the test after providing a VM - Service interconnection..


  }

  private fun getGetRadioListJobFieldReflection(): Field {
    return HomeViewModel::class.java.getDeclaredField("_getRadioListJob")
      .apply { isAccessible = true }
  }

  private fun getState(instance: HomeViewModel): MutableLiveData<HomeState> {
    return HomeViewModel::class.java.getDeclaredField("_state")
      .apply { isAccessible = true }.get(instance) as MutableLiveData<HomeState>
  }
}