package com.qubacy.hearit.application.domain.usecase.radio.add.impl

import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.RadioDomainSketch
import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.mapper._common.RadioDomainSketchDataModelMapper
import com.qubacy.hearit.application.domain.usecase.radio.add._common.AddRadioUseCase
import javax.inject.Inject

class AddRadioUseCaseImpl @Inject constructor(
  // todo: provide a repository;
  private val _mapper: RadioDomainSketchDataModelMapper
) : AddRadioUseCase {
  override suspend fun addRadio(radio: RadioDomainSketch) {
    // todo: implement:

    //_mapper.map(null, radio)
  }
}