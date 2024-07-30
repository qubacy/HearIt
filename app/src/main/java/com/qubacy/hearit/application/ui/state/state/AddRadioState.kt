package com.qubacy.hearit.application.ui.state.state

import com.qubacy.hearit.application._common.error.ErrorReference

data class AddRadioState(
  val isLoading: Boolean = false,
  val addedRadioId: Long? = null,
  val error: ErrorReference? = null
)