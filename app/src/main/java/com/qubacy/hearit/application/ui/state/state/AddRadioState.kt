package com.qubacy.hearit.application.ui.state.state

import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation

sealed class AddRadioState {
  data object Idle : AddRadioState()
  data object Loading : AddRadioState()

  data class Success(val radio: RadioPresentation) : AddRadioState()
  data class Error(val error: ErrorReference) : AddRadioState()
}