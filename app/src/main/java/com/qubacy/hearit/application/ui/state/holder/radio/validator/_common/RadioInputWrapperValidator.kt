package com.qubacy.hearit.application.ui.state.holder.radio.validator._common

import com.qubacy.hearit.application.ui.state.holder.radio.wrapper.RadioInputWrapper

interface RadioInputWrapperValidator {
  fun validate(data: RadioInputWrapper): Boolean
}