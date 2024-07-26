package com.qubacy.hearit.application.ui.state.holder.radio.wrapper.mapper._common

import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.RadioDomainSketch
import com.qubacy.hearit.application.ui.state.holder.radio.wrapper.RadioInputWrapper

interface RadioInputWrapperRadioDomainSketchMapper {
  fun map(radioInputWrapper: RadioInputWrapper): RadioDomainSketch
}