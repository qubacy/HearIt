package com.qubacy.hearit.application.ui.state.holder.radio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qubacy.hearit.application._common.error.ErrorEnum
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.state.holder.radio.validator._common.RadioInputWrapperValidator
import com.qubacy.hearit.application.ui.state.state.EditRadioState
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio._common.wrapper.RadioInputWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRadioViewModel @Inject constructor(
  private val _savedStateHandle: SavedStateHandle,
  // todo: injecting a use-case..
  private val _radioInputValidator: RadioInputWrapperValidator
) : ViewModel() {
  companion object {
    const val RADIO_ID_KEY_NAME = "radioId"
  }

  private val _radioId: Long = _savedStateHandle[RADIO_ID_KEY_NAME]!!

  private var _state: MutableLiveData<EditRadioState> = MutableLiveData(EditRadioState.Idle)
  val state: LiveData<EditRadioState> get() = _state

  //private var _loadingRadio: Flow<RadioPresentation> // todo: calling a use-case's method..
  private var _savedRadio: Flow<RadioPresentation>? = null

  /**
   * Meant to be called in Loaded state;
   */
  fun saveRadio(radioData: RadioInputWrapper) {
    if (!_radioInputValidator.validate(radioData))
      return setErrorState(ErrorEnum.RADIO_INPUT_VALIDATION_ERROR.reference)

    viewModelScope.launch {
      //_savedRadio =

      // todo: implement..
    }
  }

  private fun setErrorState(errorReference: ErrorReference) {
    _state.value = EditRadioState.Error(errorReference)
  }
}