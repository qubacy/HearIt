package com.qubacy.hearit.application.ui.state.holder.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application._common.exception.HearItException
import com.qubacy.hearit.application.data.player.model.PlayerInfoDataModel
import com.qubacy.hearit.application.data.player.repository._common.PlayerDataRepository
import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.domain.usecase.home._common.HomeUseCase
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui._common.presentation.mapper.domain._common.RadioPresentationDomainModelMapper
import com.qubacy.hearit.application.ui._common.presentation.mapper.media._common.RadioPresentationMediaItemMapper
import com.qubacy.hearit.application.ui.state.state.home.HomeState
import com.qubacy.hearit.application.ui.state.state.home.player.PlayerState
import com.qubacy.hearit.application.ui.state.state.home.player.mapper.data._common.PlayerStateInfoDataModelMapper
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

  private lateinit var _playerRepository: PlayerDataRepository
  private lateinit var _useCaseMock: HomeUseCase
  private lateinit var _radioPresentationDomainModelMapperMock: RadioPresentationDomainModelMapper
  private lateinit var _playerStateInfoDataModelMapperMock: PlayerStateInfoDataModelMapper
  private lateinit var _radioPresentationMediaItemMapperMock: RadioPresentationMediaItemMapper

  private lateinit var _instance: HomeViewModel

  @Before
  fun setup() {
    _playerRepository = mockPlayerDataRepository()
    _useCaseMock = mockUseCase()
    _radioPresentationDomainModelMapperMock = mockRadioPresentationDomainModelMapper()
    _playerStateInfoDataModelMapperMock = mockPlayerStateInfoDataModelMapper()
    _radioPresentationMediaItemMapperMock = mockRadioPresentationMediaItemMapper()

    _instance = HomeViewModel(
      _coroutineDispatcher,
      _playerRepository,
      _useCaseMock,
      _radioPresentationDomainModelMapperMock,
      _playerStateInfoDataModelMapperMock,
      _radioPresentationMediaItemMapperMock
    )
  }

  private fun mockPlayerDataRepository(): PlayerDataRepository {
    return Mockito.mock(PlayerDataRepository::class.java)
  }

  private fun mockUseCase(): HomeUseCase {
    return Mockito.mock(HomeUseCase::class.java)
  }

  private fun mockRadioPresentationDomainModelMapper(): RadioPresentationDomainModelMapper {
    return Mockito.mock(RadioPresentationDomainModelMapper::class.java)
  }

  private fun mockPlayerStateInfoDataModelMapper(): PlayerStateInfoDataModelMapper {
    return Mockito.mock(PlayerStateInfoDataModelMapper::class.java)
  }

  private fun mockRadioPresentationMediaItemMapper(): RadioPresentationMediaItemMapper {
    return Mockito.mock(RadioPresentationMediaItemMapper::class.java)
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
    _instance.observeRadioList()

    val gottenInitGetRadioListJob = getGetRadioListJobFieldReflection().get(_instance)

    Assert.assertNotNull(gottenInitGetRadioListJob)

    _instance.stopObservingRadioList()

    val gottenFinalGetRadioListJob = getGetRadioListJobFieldReflection().get(_instance)

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

    Mockito.`when`(_radioPresentationDomainModelMapperMock.map(any())).thenReturn(expectedRadioPresentation)
    Mockito.`when`(_useCaseMock.getRadioList()).thenReturn(
      flowOf(listOf(radioDomainModel))
    )

    _instance.observeRadioList()
    advanceUntilIdle()

    Mockito.verify(_radioPresentationDomainModelMapperMock).map(any())

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

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun setCurrentRadioTest() = runTest(_coroutineDispatcher) {
    val stateReference = getState(_instance)

    val radioId = 0L
    val radioPresentation = RadioPresentation(radioId, "", url = "")

    val state = HomeState(radioList = listOf(radioPresentation))

    val expectedState = state.copy(
      playerState = PlayerState(
        currentRadio = radioPresentation,
        isRadioPlaying = true
      ),
      isLoading = false
    )

    stateReference.value = expectedState

    Mockito.`when`(_playerStateInfoDataModelMapperMock.map(any()))
      .thenReturn(Mockito.mock(PlayerInfoDataModel::class.java))

    _instance.setCurrentRadio(radioId)
    advanceUntilIdle()

    val gottenState = _instance.state.value

    Assert.assertEquals(expectedState, gottenState)

    Mockito.verify(_playerStateInfoDataModelMapperMock).map(any())
    Mockito.verify(_playerRepository).updatePlayerInfo(any())
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun observePlayerInfoSucceedsTest() = runTest(_coroutineDispatcher) {
    val playerInfoDataModel = PlayerInfoDataModel(0L, true)
    val radioPresentation = RadioPresentation(0L, "", url = "")

    val expectedPlayerState = PlayerState(
      currentRadio = radioPresentation, isRadioPlaying = playerInfoDataModel.isPlaying
    )

    Mockito.`when`(_playerRepository.getPlayerInfo()).thenReturn(flowOf(playerInfoDataModel))
    Mockito.`when`(_useCaseMock.getRadio(any())).thenReturn(
      flowOf(Mockito.mock(RadioDomainModel::class.java))
    )
    Mockito.`when`(_radioPresentationDomainModelMapperMock.map(any())).thenReturn(radioPresentation)
    Mockito.`when`(_playerStateInfoDataModelMapperMock.map(any(), any())).thenReturn(expectedPlayerState)

    _instance.observePlayerInfo()
    advanceUntilIdle()

    val gottenPlayerState = getState(_instance).value!!.playerState

    Mockito.verify(_playerRepository).getPlayerInfo()
    Mockito.verify(_useCaseMock).getRadio(any())
    Mockito.verify(_radioPresentationDomainModelMapperMock).map(any())
    Mockito.verify(_playerStateInfoDataModelMapperMock).map(any(), any())

    Assert.assertEquals(expectedPlayerState, gottenPlayerState)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun observePlayerInfoThrowsExceptionTest() = runTest(_coroutineDispatcher) {
    val errorReference = ErrorReference(0L)

    val expectedState = HomeState(error = errorReference)

    Mockito.`when`(_playerRepository.getPlayerInfo()).thenReturn(flow {
      throw HearItException(errorReference)
    })

    _instance.observePlayerInfo()
    advanceUntilIdle()

    val gottenState = getState(_instance).value

    Mockito.verify(_playerRepository).getPlayerInfo()

    Assert.assertEquals(expectedState, gottenState)
  }

  @Test
  fun stopObservingPlayerInfoTest() {
    _instance.observePlayerInfo()

    val initGetPlayerInfoJob = getPlayerInfoJobFieldReflection().get(_instance)

    Assert.assertNotNull(initGetPlayerInfoJob)

    _instance.stopObservingPlayerInfo()

    val finalGetPlayerInfoJob = getPlayerInfoJobFieldReflection().get(_instance)

    Assert.assertNull(finalGetPlayerInfoJob)
  }

  private fun getGetRadioListJobFieldReflection(): Field {
    return HomeViewModel::class.java.getDeclaredField("_getRadioListJob")
      .apply { isAccessible = true }
  }

  private fun getPlayerInfoJobFieldReflection(): Field {
    return HomeViewModel::class.java.getDeclaredField("_getPlayerInfoJob")
      .apply { isAccessible = true }
  }

  private fun getState(instance: HomeViewModel): MutableLiveData<HomeState> {
    return HomeViewModel::class.java.getDeclaredField("_state")
      .apply { isAccessible = true }.get(instance) as MutableLiveData<HomeState>
  }
}