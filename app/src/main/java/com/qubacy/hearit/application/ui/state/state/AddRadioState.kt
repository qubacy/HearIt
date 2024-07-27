package com.qubacy.hearit.application.ui.state.state

import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation

data class AddRadioState(
  val isLoading: Boolean = false,
  val addedRadio: RadioPresentation? = null,
  val error: ErrorReference? = null
)