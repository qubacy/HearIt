package com.qubacy.hearit.application.ui.state.holder.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qubacy.hearit.application._common.error.ErrorEnum
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application._common.exception.HearItException
import com.qubacy.hearit.application._common.player.packet.PlayerStatePacketBody
import com.qubacy.hearit.application.data.player.repository._common.PlayerDataRepository
import com.qubacy.hearit.application.domain.usecase.home._common.HomeUseCase
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui._common.presentation.mapper.domain._common.RadioPresentationDomainModelMapper
import com.qubacy.hearit.application.ui._common.presentation.mapper.media._common.RadioPresentationMediaItemMapper
import com.qubacy.hearit.application.ui.state.holder._common.dispatcher._di.ViewModelDispatcherQualifier
import com.qubacy.hearit.application.ui.state.state.home.HomeState
import com.qubacy.hearit.application.ui.state.state.home.player.PlayerState
import com.qubacy.hearit.application.ui.state.state.home.player.PlayerStatePreservable
import com.qubacy.hearit.application.ui.state.state.home.player.mapper.data._common.PlayerStateInfoDataModelMapper
import com.qubacy.hearit.application.ui.state.state.home.player.mapper.player._common.PlayerStateStatePacketBodyMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val _savedStateHandle: SavedStateHandle,
  @ViewModelDispatcherQualifier
  private val _dispatcher: CoroutineDispatcher,
  //private val _playerRepository: PlayerDataRepository,
  private val _useCase: HomeUseCase,
  private val _radioPresentationDomainModelMapper: RadioPresentationDomainModelMapper,
  //private val _playerStateInfoDataModelMapper: PlayerStateInfoDataModelMapper,
  //private val _radioPresentationMediaItemMapper: RadioPresentationMediaItemMapper,
  private val _playerStateStatePacketBodyMapper: PlayerStateStatePacketBodyMapper
) : ViewModel() {
  companion object {
    const val TAG = "HomeViewModel"

//    const val PLAYER_STATE_KEY = "playerState"
    const val PLAYER_STATE_INIT_FLAG_KEY = "playerStateInitFlag"
  }

  private val _state: MutableLiveData<HomeState> = MutableLiveData(HomeState())
  val state: LiveData<HomeState> get() = _state

  private val _playerState: MutableLiveData<PlayerState> = MutableLiveData(PlayerState())
  val playerState: LiveData<PlayerState> get() = _playerState

  private val _playerStatePacketBody: MutableLiveData<PlayerStatePacketBody>
  val playerStatePacketBody: LiveData<PlayerStatePacketBody> get() = _playerStatePacketBody

  private var _getCurrentRadioJob: Job? = null
  private var _getRadioListJob: Job? = null
  //private var _getPlayerInfoJob: Job? = null

  init {
    _savedStateHandle.get<Boolean>(PLAYER_STATE_INIT_FLAG_KEY).let {
      _playerStatePacketBody =
        if (it != null) MutableLiveData()
        else { MutableLiveData(_playerStateStatePacketBodyMapper.map(_playerState.value!!)) }
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

//  fun observePlayerInfo() {
//    if (_getPlayerInfoJob != null) return
//
//    _getPlayerInfoJob = startGettingPlayerInfo()
//  }

//  fun stopObservingPlayerInfo() {
//    if (_getPlayerInfoJob == null) return
//
//    disposeGetPlayerInfoJob()
//  }

  private fun disposeGetRadioListJob() {
    _getRadioListJob!!.cancel()
    _getRadioListJob = null
  }

//  private fun disposeGetPlayerInfoJob() {
//    _getPlayerInfoJob!!.cancel()
//    _getPlayerInfoJob = null
//  }

  private fun disposeGetCurrentRadioJob() {
    _getCurrentRadioJob?.let {
      it.cancel()

      _getCurrentRadioJob = null
    }
  }

  override fun onCleared() {
    stopObservingRadioList()
    //stopObservingPlayerInfo()
    disposeGetCurrentRadioJob()

    super.onCleared()
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

//  private fun startGettingPlayerInfo(): Job {
//    return viewModelScope.launch(_dispatcher) {
//      _playerRepository.getPlayerInfo().map {
//        val radioDomainModel = it.curRadioId?.let { radioId -> _useCase.getRadio(radioId).first() }
//        val radioPresentation = radioDomainModel
//          ?.let { _radioPresentationDomainModelMapper.map(radioDomainModel) }
//
//        _playerStateInfoDataModelMapper.map(radioPresentation, it)
//      }.onEach {
//        _state.postValue(_state.value!!.copy(playerState = it))
//      }.catch { cause ->
//        if (cause !is HearItException) throw cause
//
//        setErrorState(cause.errorReference)
//      }.collect()
//    }
//  }

//  @Deprecated("There's no need to preserve PlayerState anymore. Therefore, the method is no use;")
//  private fun resolvePlayerStatePreservable(
//    playerStatePreservable: PlayerStatePreservable
//  ): Job? {
//    if (playerStatePreservable.currentRadioId == null) return null
//
//    _state.value = _state.value!!.copy(isLoading = true)
//
//    return viewModelScope.launch(_dispatcher) {
//      _useCase.getRadio(playerStatePreservable.currentRadioId).map {
//        _radioPresentationDomainModelMapper.map(it)
//      }.onEach {
//        _state.postValue(_state.value!!.copy(
//          playerState = PlayerState(currentRadio = it, playerStatePreservable.isRadioPlaying),
//          isLoading = false
//        ))
//      }.catch { cause ->
//        if (cause !is HearItException) throw cause
//
//        setErrorState(cause.errorReference)
//      }.collect()
//    }
//  }

  /**
   * It's supposed that the radio with the provided [id] is already loaded;
   */
  fun setCurrentRadio(id: Long) {
    val radioPresentation = _state.value!!.radioList?.find { it.id == id }

    if (radioPresentation == null) {
      _state.value = _state.value!!.copy(error = ErrorEnum.RADIO_NOT_FOUND.reference)

      return
    }

    val playerState = _playerState.value!!.copy(currentRadio = radioPresentation)

    updatePlayerState(playerState)

//    val playerState = _state.value!!.playerState?.copy(currentRadio = radioPresentation)
//      ?: PlayerState(currentRadio = radioPresentation, isRadioPlaying = true)
//
//    _state.value = _state.value!!.copy(playerState = playerState)
//
//    viewModelScope.launch(_dispatcher) {
//      _playerRepository.updatePlayerInfo(_playerStateInfoDataModelMapper.map(playerState))
//    }
  }

  fun setPrevRadio() {
    setNeighborRadio(true)
  }

  fun changePlayingState() {
    val playerState = _playerState.value!!.let {
      it.copy(isRadioPlaying = !it.isRadioPlaying)
    }

    updatePlayerState(playerState)

//    val stateSnapshot = _state.value!!
//    val playerStateSnapshot = stateSnapshot.playerState!!
//
//    _state.value = stateSnapshot.copy(
//      playerState = playerStateSnapshot.copy(isRadioPlaying = !playerStateSnapshot.isRadioPlaying)
//    )
  }

  fun setNextRadio() {
    setNeighborRadio(false)
  }

  private fun setNeighborRadio(isPrev: Boolean) {
    val radioList = _state.value!!.radioList
    val playerState = _playerState.value!!
    val curRadioPresentation = playerState.currentRadio

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

    updatePlayerState(playerState.copy(currentRadio = currentRadio))
  }

  fun resolvePlayerStatePacketBody(playerStatePacketBody: PlayerStatePacketBody) {
    // todo: implement..


  }

  fun consumeCurrentError() {
    _state.value = _state.value!!.copy(error = null)
  }

//  // todo: rethink this. isn't it a bad approach?:
//  fun getRadioPresentationMediaItemMapper(): RadioPresentationMediaItemMapper {
//    return _radioPresentationMediaItemMapper
//  }

  private fun setErrorState(errorReference: ErrorReference) {
    _state.postValue(_state.value!!.copy(error = errorReference, isLoading = false))
  }

  private fun updatePlayerState(playerState: PlayerState) {
    _playerState.value = playerState
    _playerStatePacketBody.value = _playerStateStatePacketBodyMapper.map(playerState)
  }
}