package com.qubacy.hearit.application.ui.state.state

import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation

data class PlayerState(
  val currentRadio: RadioPresentation? = null,
  val isRadioPlaying: Boolean = false
)

data class HomeState(
  val isLoading: Boolean = false,
  val radioList: List<RadioPresentation>? = null,
  val playerState: PlayerState? = null,
  val error: ErrorReference? = null
)