package com.qubacy.hearit.application.domain.usecase.radio.edit.impl

import com.qubacy.hearit.application.data.radio.repository._common.RadioDataRepository
import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.RadioDomainSketch
import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.mapper._common.RadioDomainSketchDataModelMapper
import com.qubacy.hearit.application.domain.usecase.radio.edit._common.EditRadioUseCase
import com.qubacy.hearit.application.domain.usecase.radio.get._common.GetRadioUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EditRadioUseCaseImpl @Inject constructor(
  private val _repository: RadioDataRepository,
  private val _getRadioUseCase: GetRadioUseCase,
  private val _domainSketchDataModelMapper: RadioDomainSketchDataModelMapper
) : EditRadioUseCase {
  companion object {
    const val TAG = "EditRadioUseCaseImpl"
  }

  override suspend fun getRadio(
    radioId: Long
  ): Flow<RadioDomainModel> {
    return _getRadioUseCase.getRadio(radioId)
  }

  override suspend fun saveRadio(
    radioId: Long,
    radio: RadioDomainSketch
  ) {
    return _repository.updateRadio(_domainSketchDataModelMapper.map(radioId, radio))
  }
}