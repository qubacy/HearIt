package com.qubacy.hearit.application.ui.state.holder.radio.wrapper.mapper.impl

import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.RadioDomainSketch
import com.qubacy.hearit.application.ui.state.holder.radio.wrapper.RadioInputWrapper
import com.qubacy.hearit.application.ui.state.holder.radio.wrapper.mapper._common.RadioInputWrapperRadioDomainSketchMapper
import javax.inject.Inject

class RadioInputWrapperRadioDomainSketchMapperImpl @Inject constructor(

) : RadioInputWrapperRadioDomainSketchMapper {
  override fun map(radioInputWrapper: RadioInputWrapper): RadioDomainSketch {
    return RadioDomainSketch(
      radioInputWrapper.title,
      radioInputWrapper.description,
      radioInputWrapper.coverUri?.toString(),
      radioInputWrapper.url
    )
  }
}