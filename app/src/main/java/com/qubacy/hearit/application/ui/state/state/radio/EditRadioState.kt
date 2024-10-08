package com.qubacy.hearit.application.ui.state.state.radio

import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation

data class EditRadioState(
  val isLoading: Boolean = false,
  val loadedRadio: RadioPresentation? = null,
  val savedRadioId: Long? = null,
  val error: ErrorReference? = null
)