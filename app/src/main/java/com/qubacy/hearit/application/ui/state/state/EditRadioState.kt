package com.qubacy.hearit.application.ui.state.state

import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation

sealed class EditRadioState {
  data object Idle : EditRadioState()
  data object Loading : EditRadioState()

  data class Loaded(val radio: RadioPresentation) : EditRadioState()

  data class Success(val radio: RadioPresentation) : EditRadioState()
  data class Error(val error: ErrorReference) : EditRadioState()
}