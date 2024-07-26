package com.qubacy.hearit.application.ui.state.holder.radio

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qubacy.hearit.application._common.error.ErrorEnum
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application._common.exception.HearItException
import com.qubacy.hearit.application.domain.usecase.radio.edit._common.EditRadioUseCase
import com.qubacy.hearit.application.ui._common.presentation.mapper._common.RadioDomainModelRadioPresentationMapper
import com.qubacy.hearit.application.ui.state.holder._common.dispatcher._di.ViewModelDispatcherQualifier
import com.qubacy.hearit.application.ui.state.holder.radio.validator._common.RadioInputWrapperValidator
import com.qubacy.hearit.application.ui.state.state.EditRadioState
import com.qubacy.hearit.application.ui.state.holder.radio.wrapper.RadioInputWrapper
import com.qubacy.hearit.application.ui.state.holder.radio.wrapper.mapper._common.RadioInputWrapperRadioDomainSketchMapper
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
class EditRadioViewModel @Inject constructor(
  private val _savedStateHandle: SavedStateHandle,
  @ViewModelDispatcherQualifier
  private val _dispatcher: CoroutineDispatcher,
  private val _useCase: EditRadioUseCase,
  private val _radioInputValidator: RadioInputWrapperValidator,
  private val _radioMapper: RadioDomainModelRadioPresentationMapper,
  private val _radioInputWrapperDomainSketchMapper: RadioInputWrapperRadioDomainSketchMapper
) : ViewModel() {
  companion object {
    const val TAG = "EditRadioViewModel"

    const val RADIO_ID_KEY_NAME = "radioId"
  }

  private var _state: MutableLiveData<EditRadioState> = MutableLiveData(EditRadioState.Idle)
  val state: LiveData<EditRadioState> get() = _state

  private var _getRadioJob: Job? = null
  private var _saveRadioJob: Job? = null

  fun observeRadio() {
    if (_getRadioJob != null) return

    Log.d(TAG, "observeRadio();")

    _getRadioJob = startGettingRadio()
  }

  fun stopObservingRadio() {
    if (_getRadioJob == null) return

    Log.d(TAG, "stopObservingRadio();")

    disposeGetRadioJob()
  }

  private fun disposeGetRadioJob() {
    _getRadioJob!!.cancel()
    _getRadioJob = null
  }

  override fun onCleared() {
    stopObservingRadio()

    _saveRadioJob?.apply {
      cancel()

      _saveRadioJob = null
    }

    super.onCleared()
  }

  private fun startGettingRadio(): Job {
    val radioId: Long = _savedStateHandle[RADIO_ID_KEY_NAME]!!

    return viewModelScope.launch(_dispatcher) {
      _useCase.getRadio(radioId).map {
        _radioMapper.map(it)
      }.onEach {
        _state.value = EditRadioState.Loaded(it)
      }.catch { cause ->
        if (cause !is HearItException) throw cause

        setErrorState(cause.errorReference)
      }.collect()
    }
  }

  /**
   * Meant to be called in the Loaded state;
   */
  fun saveRadio(radioData: RadioInputWrapper) {
    val stateSnapshot = _state.value

    if (stateSnapshot !is EditRadioState.Loaded) return
    if (!_radioInputValidator.validate(radioData))
      return setErrorState(ErrorEnum.RADIO_INPUT_VALIDATION_ERROR.reference)

    _state.value = EditRadioState.Loading
    _saveRadioJob = startSavingRadio(stateSnapshot.radio.id, radioData)
  }

  private fun startSavingRadio(radioId: Long, radioData: RadioInputWrapper): Job {
    return viewModelScope.launch(_dispatcher) {
      _useCase.saveRadio(radioId, _radioInputWrapperDomainSketchMapper.map(radioData)).map {
        _radioMapper.map(it)
      }.onEach {
        _state.value = EditRadioState.Success(it)
      }.catch { cause ->
        if (cause !is HearItException) throw cause

        setErrorState(cause.errorReference)
      }.collect()
    }
  }

  private fun setErrorState(errorReference: ErrorReference) {
    _state.postValue(EditRadioState.Error(errorReference))
  }
}