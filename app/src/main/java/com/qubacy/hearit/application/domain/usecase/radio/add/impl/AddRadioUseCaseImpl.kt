package com.qubacy.hearit.application.domain.usecase.radio.add.impl

import com.qubacy.hearit.application.data.radio.repository._common.RadioDataRepository
import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.RadioDomainSketch
import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.mapper._common.RadioDomainSketchDataModelMapper
import com.qubacy.hearit.application.domain.usecase.radio.add._common.AddRadioUseCase
import javax.inject.Inject

class AddRadioUseCaseImpl @Inject constructor(
  private val _repository: RadioDataRepository,
  private val _mapper: RadioDomainSketchDataModelMapper
) : AddRadioUseCase {
  override suspend fun addRadio(radio: RadioDomainSketch): Long {
    return _repository.addRadio(_mapper.map(null, radio))
  }
}