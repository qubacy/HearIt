package com.qubacy.hearit.application.ui.state.state.home

import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.state.state.home.player.PlayerState

data class HomeState(
  val isLoading: Boolean = false,
  val radioList: List<RadioPresentation>? = null,
  val error: ErrorReference? = null
)