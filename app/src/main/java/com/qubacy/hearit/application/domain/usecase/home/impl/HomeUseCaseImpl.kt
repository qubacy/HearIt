package com.qubacy.hearit.application.domain.usecase.home.impl

import com.qubacy.hearit.application.data.radio.repository._common.RadioDataRepository
import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.domain._common.model.mapper.data._common.RadioDomainModelDataModelMapper
import com.qubacy.hearit.application.domain.usecase.home._common.HomeUseCase
import com.qubacy.hearit.application.domain.usecase.radio.get._common.GetRadioUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeUseCaseImpl @Inject constructor(
  private val _repository: RadioDataRepository,
  private val _getRadioUseCase: GetRadioUseCase,
  private val _mapper: RadioDomainModelDataModelMapper
) : HomeUseCase {
  override suspend fun getRadio(radioId: Long): Flow<RadioDomainModel> {
    return _getRadioUseCase.getRadio(radioId)
  }

  override suspend fun getRadioList(): Flow<List<RadioDomainModel>> {
    return _repository.getAllRadios().map { list -> list.map { _mapper.map(it) } }
  }
}