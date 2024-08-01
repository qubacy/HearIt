package com.qubacy.hearit.application.domain.usecase.radio.get.impl

import com.qubacy.hearit.application.data.radio.repository._common.RadioDataRepository
import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.domain._common.model.mapper._common.RadioDomainModelDataModelMapper
import com.qubacy.hearit.application.domain.usecase.radio.get._common.GetRadioUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetRadioUseCaseImpl @Inject constructor(
  private val _repository: RadioDataRepository,
  private val _domainModelDataModelMapper: RadioDomainModelDataModelMapper
) : GetRadioUseCase {
  override suspend fun getRadio(radioId: Long): Flow<RadioDomainModel> {
    return _repository.getRadio(radioId).map { _domainModelDataModelMapper.map(it) }
  }
}