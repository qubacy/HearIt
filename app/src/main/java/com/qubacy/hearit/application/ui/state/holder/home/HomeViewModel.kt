package com.qubacy.hearit.application.ui.state.holder.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application._common.exception.HearItException
import com.qubacy.hearit.application._common.player.packet.PlayerStatePacketBody
import com.qubacy.hearit.application.domain.usecase.home._common.HomeUseCase
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui._common.presentation.mapper.domain._common.RadioPresentationDomainModelMapper
import com.qubacy.hearit.application.ui.state.holder._common.dispatcher._di.ViewModelDispatcherQualifier
import com.qubacy.hearit.application.ui.state.state.home.HomeState
import com.qubacy.hearit.application.ui.state.state.home.player.PlayerState
import com.qubacy.hearit.application.ui.state.state.home.player.mapper.player._common.PlayerStateStatePacketBodyMapper
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
  private val _radioPresentationDomainModelMapper: RadioPresentationDomainModelMapper,
  private val _playerStateStatePacketBodyMapper: PlayerStateStatePacketBodyMapper
) : ViewModel() {
  companion object {
    const val TAG = "HomeViewModel"
  }

  private val _state: MutableLiveData<HomeState> = MutableLiveData(HomeState())
  val state: LiveData<HomeState> get() = _state

  private val _playerState: MutableLiveData<PlayerState> = MutableLiveData(PlayerState())
  val playerState: LiveData<PlayerState> get() = _playerState

  private val _playerStatePacketBody: MutableLiveData<PlayerStatePacketBody?> = MutableLiveData(null)
  val playerStatePacketBody: LiveData<PlayerStatePacketBody?> get() = _playerStatePacketBody

  private var _getCurrentRadioJob: Job? = null
  private var _getRadioListJob: Job? = null

  private fun observeCurRadio(radioId: Long) {
    disposeGetCurrentRadioJob()

    _getCurrentRadioJob = startGettingCurRadio(radioId)
  }

  fun observeRadioList() {
    if (_getRadioListJob != null) return

    _getRadioListJob = startGettingRadioList()
  }

  fun stopObservingRadioList() {
    if (_getRadioListJob == null) return

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

    super.onCleared()
  }

  private fun startGettingCurRadio(radioId: Long): Job {
    return viewModelScope.launch(_dispatcher) {
      _useCase.getRadio(radioId).map {
        _radioPresentationDomainModelMapper.map(it)
      }.onEach {
        Log.d(TAG, "startGettingCurRadio(): onEach(): curRadio = $it;")

        _playerState.postValue(_playerState.value!!.let { prevState ->
          prevState.copy(
            currentRadio = it,
            isRadioPlaying = prevState.isRadioPlaying
          )
        })
      }.collect()
    }
  }

  private fun startGettingRadioList(): Job {
    _state.value = _state.value!!.copy(isLoading = true)

    return viewModelScope.launch(_dispatcher) {
      _useCase.getRadioList().map { list ->
        list.map { _radioPresentationDomainModelMapper.map(it) }
      }.onEach {
        // todo: possible bug:
        _state.postValue(_state.value!!.copy(radioList = it, isLoading = false))
      }.catch { cause ->
        if (cause !is HearItException) throw cause

        setErrorState(cause.errorReference)
      }.collect()
    }
  }

  fun setCurrentRadio(id: Long) {
    setPlayerState(id).let { broadcastPlayerState(it, id) }
  }

  /**
   * It's supposed that the radio with the provided [id] is already loaded;
   */
  private fun setPlayerState(
    radioId: Long? = _playerState.value?.currentRadio?.id,
    isPlaying: Boolean = _playerState.value?.isRadioPlaying ?: false
  ): PlayerState {
    var curRadio: RadioPresentation? = _playerState.value?.currentRadio

    if (radioId != null && radioId != curRadio?.id) {
      observeCurRadio(radioId)
    } else if (radioId == null) {
      curRadio = null
    }

    val playerState = _playerState.value!!.copy(
      currentRadio = curRadio,
      isRadioPlaying = isPlaying
    )

    _playerState.value = playerState

    return playerState
  }

  fun setPrevRadio() {
    setNeighborRadio(true)
  }

  fun changePlayingState() {
    setPlayerState(isPlaying = !_playerState.value!!.isRadioPlaying)
      .let { broadcastPlayerState(it) }
  }

  fun setNextRadio() {
    setNeighborRadio(false)
  }

  private fun setNeighborRadio(isPrev: Boolean) {
    val radioList = _state.value!!.radioList
    val playerState = _playerState.value!!
    val curRadioPresentation = playerState.currentRadio

    var currentRadioId: Long? = null

    if (curRadioPresentation == null) {
      currentRadioId = radioList?.first()?.id
    } else {
      radioList!!

      val curRadioIndex = radioList.indexOfFirst { it.id == curRadioPresentation.id }
      val neighborRadioIndex =
        if (isPrev) if (curRadioIndex == 0) radioList.size - 1 else curRadioIndex - 1
        else (curRadioIndex + 1) % radioList.size

      currentRadioId = radioList[neighborRadioIndex].id
    }

    setPlayerState(radioId = currentRadioId).let { broadcastPlayerState(it, currentRadioId) }
  }

  fun resolvePlayerStatePacketBody(playerStatePacketBody: PlayerStatePacketBody) {
    setPlayerState(playerStatePacketBody.radioId, playerStatePacketBody.isPlaying)
  }

  fun consumeCurrentError() {
    _state.value = _state.value!!.copy(error = null)
  }

  private fun setErrorState(errorReference: ErrorReference) {
    _state.postValue(_state.value!!.copy(error = errorReference, isLoading = false))
  }

  private fun broadcastPlayerState(playerState: PlayerState, newRadioId: Long? = null) {
    _playerStatePacketBody.value = _playerStateStatePacketBodyMapper.map(playerState, newRadioId)
  }
}