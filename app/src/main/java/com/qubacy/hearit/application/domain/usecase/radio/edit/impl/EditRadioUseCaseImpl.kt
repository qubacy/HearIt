package com.qubacy.hearit.application.domain.usecase.radio.edit.impl

import com.qubacy.hearit.application.data.radio.repository._common.RadioDataRepository
import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.RadioDomainSketch
import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.mapper._common.RadioDomainSketchDataModelMapper
import com.qubacy.hearit.application.domain.usecase.radio.edit._common.EditRadioUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EditRadioUseCaseImpl @Inject constructor(
  private val _repository: RadioDataRepository,
  private val _mapper: RadioDomainSketchDataModelMapper
) : EditRadioUseCase {
  override suspend fun getRadio(
    radioId: Long
  ): Flow<RadioDomainModel> {
    // todo: implement:

    return flow {  }
  }

  override suspend fun saveRadio(
    radioId: Long,
    radio: RadioDomainSketch
  ) {
    return _repository.updateRadio(_mapper.map(radioId, radio))
  }
}