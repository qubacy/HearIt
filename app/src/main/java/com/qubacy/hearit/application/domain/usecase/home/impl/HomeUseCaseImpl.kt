package com.qubacy.hearit.application.domain.usecase.home.impl

import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.domain.usecase.home._common.HomeUseCase
import com.qubacy.hearit.application.domain.usecase.radio.get._common.GetRadioUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeUseCaseImpl @Inject constructor(
  private val _getRadioUseCase: GetRadioUseCase
) : HomeUseCase {
  override suspend fun getRadio(radioId: Long): Flow<RadioDomainModel> {
    return _getRadioUseCase.getRadio(radioId)
  }

  override suspend fun getRadioList(): Flow<List<RadioDomainModel>> {
    return _getRadioUseCase.getRadioList()
  }
}