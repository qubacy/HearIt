package com.qubacy.hearit.application.ui.state.holder.radio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qubacy.hearit.application._common.error.ErrorEnum
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.state.holder.radio.validator._common.RadioInputWrapperValidator
import com.qubacy.hearit.application.ui.state.state.AddRadioState
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio._common.wrapper.RadioInputWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRadioViewModel @Inject constructor(
  // todo: injecting a use-case..
  private val _radioInputValidator: RadioInputWrapperValidator
) : ViewModel() {
  private var _state: MutableLiveData<AddRadioState> = MutableLiveData(AddRadioState.Idle)
  val state: LiveData<AddRadioState> get() = _state

  private var _addedRadio: Flow<RadioPresentation>? = null

  fun addRadio(radioData: RadioInputWrapper) {
    if (!_radioInputValidator.validate(radioData))
      return setErrorState(ErrorEnum.RADIO_INPUT_VALIDATION_ERROR.reference)

    viewModelScope.launch {
      //_addedRadio =

      // todo: implement..
    }
  }

  private fun setErrorState(errorReference: ErrorReference) {
    _state.value = AddRadioState.Error(errorReference)
  }
}