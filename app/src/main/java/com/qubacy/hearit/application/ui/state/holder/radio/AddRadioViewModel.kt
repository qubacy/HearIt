package com.qubacy.hearit.application.ui.state.holder.radio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qubacy.hearit.application._common.error.ErrorEnum
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application._common.exception.HearItException
import com.qubacy.hearit.application.domain.usecase.radio.add._common.AddRadioUseCase
import com.qubacy.hearit.application.ui._common.presentation.mapper._common.RadioDomainModelRadioPresentationMapper
import com.qubacy.hearit.application.ui.state.holder._common.dispatcher._di.ViewModelDispatcherQualifier
import com.qubacy.hearit.application.ui.state.holder.radio.validator._common.RadioInputWrapperValidator
import com.qubacy.hearit.application.ui.state.state.AddRadioState
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
class AddRadioViewModel @Inject constructor(
  @ViewModelDispatcherQualifier
  private val _dispatcher: CoroutineDispatcher,
  private val _useCase: AddRadioUseCase,
  private val _radioInputValidator: RadioInputWrapperValidator,
  private val _radioDomainModelPresentationMapper: RadioDomainModelRadioPresentationMapper,
  private val _radioInputWrapperDomainSketchMapper: RadioInputWrapperRadioDomainSketchMapper
) : ViewModel() {
  private var _state: MutableLiveData<AddRadioState> = MutableLiveData(AddRadioState())
  val state: LiveData<AddRadioState> get() = _state

  private var _addRadioJob: Job? = null

  override fun onCleared() {
    _addRadioJob?.apply {
      cancel()

      _addRadioJob = null
    }

    super.onCleared()
  }

  fun addRadio(radioData: RadioInputWrapper) {
    if (!_radioInputValidator.validate(radioData))
      return setErrorState(ErrorEnum.RADIO_INPUT_VALIDATION_ERROR.reference)

    _state.value = _state.value!!.copy(isLoading = true)
    _addRadioJob = startAddingRadio(radioData)
  }

  private fun startAddingRadio(radioData: RadioInputWrapper): Job {
    return viewModelScope.launch(_dispatcher) {
      _useCase.addRadio(_radioInputWrapperDomainSketchMapper.map(radioData)).map {
        _radioDomainModelPresentationMapper.map(it)
      }.onEach {
        _state.postValue(_state.value!!.copy(addedRadio = it, isLoading = false))
      }.catch { cause ->
        if (cause !is HearItException) throw cause

        setErrorState(cause.errorReference)
      }.collect()
    }
  }

  fun consumeCurrentError() {
    _state.value = _state.value!!.copy(error = null)
  }

  private fun setErrorState(errorReference: ErrorReference) {
    _state.postValue(_state.value!!.copy(error = errorReference, isLoading = false))
  }
}