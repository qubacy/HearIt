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
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio._common.wrapper.RadioInputWrapper
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
  private val _radioMapper: RadioDomainModelRadioPresentationMapper
) : ViewModel() {
  private var _state: MutableLiveData<AddRadioState> = MutableLiveData(AddRadioState.Idle)
  val state: LiveData<AddRadioState> get() = _state

  private var _addRadioJob: Job? = null

  fun addRadio(radioData: RadioInputWrapper) {
    if (!_radioInputValidator.validate(radioData))
      return setErrorState(ErrorEnum.RADIO_INPUT_VALIDATION_ERROR.reference)

    _state.value = AddRadioState.Loading
    _addRadioJob = startAddingRadio(radioData)
  }

  private fun startAddingRadio(radioData: RadioInputWrapper): Job {
    return viewModelScope.launch(_dispatcher) {
      _useCase.addRadio(radioData.toRadioDomainSketch()).map {
        _radioMapper.map(it)
      }.onEach {
        _state.value = AddRadioState.Success(it)
      }.catch { cause ->
        if (cause !is HearItException) throw cause

        setErrorState(cause.errorReference)
      }.collect()
    }
  }

  private fun setErrorState(errorReference: ErrorReference) {
    _state.postValue(AddRadioState.Error(errorReference))
  }
}