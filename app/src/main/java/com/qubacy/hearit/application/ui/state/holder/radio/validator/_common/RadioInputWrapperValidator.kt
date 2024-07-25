package com.qubacy.hearit.application.ui.state.holder.radio.validator._common

import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio._common.wrapper.RadioInputWrapper

interface RadioInputWrapperValidator {
  fun validate(data: RadioInputWrapper): Boolean
}