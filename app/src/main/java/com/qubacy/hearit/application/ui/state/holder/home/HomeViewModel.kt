package com.qubacy.hearit.application.ui.state.holder.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qubacy.hearit.application._common.error.ErrorEnum
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application._common.exception.HearItException
import com.qubacy.hearit.application.domain.usecase.home._common.HomeUseCase
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui._common.presentation.mapper._common.RadioDomainModelRadioPresentationMapper
import com.qubacy.hearit.application.ui.state.holder._common.dispatcher._di.ViewModelDispatcherQualifier
import com.qubacy.hearit.application.ui.state.state.HomeState
import com.qubacy.hearit.application.ui.state.state.PlayerState
import com.qubacy.hearit.application.ui.state.state.PlayerStatePreservable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val _savedStateHandle: SavedStateHandle,
  @ViewModelDispatcherQualifier
  private val _dispatcher: CoroutineDispatcher,
  private val _useCase: HomeUseCase,
  private val _radioMapper: RadioDomainModelRadioPresentationMapper
) : ViewModel() {
  companion object {
    const val TAG = "HomeViewModel"

    const val PLAYER_STATE_KEY = "playerState"
  }

  private val _state: MutableLiveData<HomeState> = MutableLiveData(HomeState())
  val state: LiveData<HomeState> get() = _state

  private var _getCurrentRadioJob: Job? = null
  private var _getRadioListJob: Job? = null

  init {
    _savedStateHandle.get<PlayerStatePreservable?>(PLAYER_STATE_KEY)?.let {
      _getCurrentRadioJob = resolvePlayerStatePreservable(it)
    }
  }

  fun observeRadioList() {
    if (_getRadioListJob != null) return

    //Log.d(TAG, "observeRadioList();")

    _getRadioListJob = startGettingRadioList()
  }

  fun stopObservingRadioList() {
    if (_getRadioListJob == null) return

    //Log.d(TAG, "stopObservingRadioList();")

    disposeGetRadioListJob()
  }

  private fun disposeGetRadioListJob() {
    _getRadioListJob!!.cancel()
    _getRadioListJob = null
  }

  private fun disposeGetCurrentRadioJob() {
    _getCurrentRadioJob?.let {
      it.cancel()

      _getCurrentRadioJob = null
    }
  }

  override fun onCleared() {
    stopObservingRadioList()
    disposeGetCurrentRadioJob()

    _state.value?.playerState?.let {
      _savedStateHandle[PLAYER_STATE_KEY] = it.toPlayerStatePreservable()
    }

    super.onCleared()
  }

  private fun startGettingRadioList(): Job {
    _state.value = _state.value!!.copy(isLoading = true)

    return viewModelScope.launch(_dispatcher) {
      _useCase.getRadioList().map { list ->
        list.map { _radioMapper.map(it) }
      }.onEach {
        // todo: possible bug:
        _state.postValue(_state.value!!.copy(radioList = it, isLoading = false))
      }.catch { cause ->
        if (cause !is HearItException) throw cause

        setErrorState(cause.errorReference)
      }.collect()
    }
  }

  private fun resolvePlayerStatePreservable(
    playerStatePreservable: PlayerStatePreservable
  ): Job? {
    if (playerStatePreservable.currentRadioId == null) return null

    _state.value = _state.value!!.copy(isLoading = true)

    return viewModelScope.launch(_dispatcher) {
      _useCase.getRadio(playerStatePreservable.currentRadioId).map {
        _radioMapper.map(it)
      }.onEach {
        _state.postValue(_state.value!!.copy(
          playerState = PlayerState(currentRadio = it, playerStatePreservable.isRadioPlaying),
          isLoading = false
        ))
      }.catch { cause ->
        if (cause !is HearItException) throw cause

        setErrorState(cause.errorReference)
      }.collect()
    }
  }

  /**
   * It's supposed that the radio with the provided [id] is already loaded;
   */
  fun setCurrentRadio(id: Long) {
    val radioPresentation = _state.value!!.radioList?.find { it.id == id }

    if (radioPresentation == null) {
      _state.value = _state.value!!.copy(error = ErrorEnum.RADIO_NOT_FOUND.reference)

      return
    }

    val playerState = _state.value!!.playerState?.copy(currentRadio = radioPresentation)
      ?: PlayerState(currentRadio = radioPresentation, isRadioPlaying = true)

    _state.value = _state.value!!.copy(playerState = playerState)
  }

  fun setPrevRadio() {
    setNeighborRadio(true)
  }

  fun changePlayingState() {
    val stateSnapshot = _state.value!!
    val playerStateSnapshot = stateSnapshot.playerState!!

    _state.value = stateSnapshot.copy(
      playerState = playerStateSnapshot.copy(isRadioPlaying = !playerStateSnapshot.isRadioPlaying)
    )
  }

  fun setNextRadio() {
    setNeighborRadio(false)
  }

  private fun setNeighborRadio(isPrev: Boolean) {
    val stateSnapshot = _state.value!!

    val radioList = stateSnapshot.radioList
    val playerState = stateSnapshot.playerState
    val curRadioPresentation = playerState?.currentRadio

    var currentRadio: RadioPresentation? = null

    if (curRadioPresentation == null) {
      currentRadio = radioList?.first()
    } else {
      radioList!!

      val curRadioIndex = radioList.indexOfFirst { it.id == curRadioPresentation.id }
      val neighborRadioIndex =
        if (isPrev) if (curRadioIndex == 0) radioList.size - 1 else curRadioIndex - 1
        else (curRadioIndex + 1) % radioList.size

      currentRadio = radioList[neighborRadioIndex]
    }

    _state.value = stateSnapshot.copy(
      playerState = stateSnapshot.playerState?.copy(currentRadio = currentRadio)
    )
  }

  fun consumeCurrentError() {
    _state.value = _state.value!!.copy(error = null)
  }

  private fun setErrorState(errorReference: ErrorReference) {
    _state.postValue(_state.value!!.copy(error = errorReference, isLoading = false))
  }
}