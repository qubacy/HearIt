package com.qubacy.hearit.application.domain.usecase.radio.edit.impl

import com.qubacy.hearit.application.data.radio.repository._common.RadioDataRepository
import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.domain._common.model.mapper._common.RadioDomainModelDataModelMapper
import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.RadioDomainSketch
import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.mapper._common.RadioDomainSketchDataModelMapper
import com.qubacy.hearit.application.domain.usecase.radio.edit._common.EditRadioUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EditRadioUseCaseImpl @Inject constructor(
  private val _repository: RadioDataRepository,
  private val _domainSketchDataModelMapper: RadioDomainSketchDataModelMapper,
  private val _domainModelDataModelMapper: RadioDomainModelDataModelMapper
) : EditRadioUseCase {
  companion object {
    const val TAG = "EditRadioUseCaseImpl"
  }

  override suspend fun getRadio(
    radioId: Long
  ): Flow<RadioDomainModel> {
    return _repository.getRadio(radioId).map { _domainModelDataModelMapper.map(it) }
  }

  override suspend fun saveRadio(
    radioId: Long,
    radio: RadioDomainSketch
  ) {
    return _repository.updateRadio(_domainSketchDataModelMapper.map(radioId, radio))
  }
}