package com.qubacy.hearit.application.ui.state.state

import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation

sealed class HomeState {
  data object Idle : HomeState()
  data object Loading : HomeState()

  data class Error(val error: ErrorReference) : HomeState()
  data class Success(val radioList: List<RadioPresentation>) : HomeState()
}