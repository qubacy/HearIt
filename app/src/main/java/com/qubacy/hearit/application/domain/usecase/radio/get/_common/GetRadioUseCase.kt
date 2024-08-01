package com.qubacy.hearit.application.domain.usecase.radio.get._common

import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import kotlinx.coroutines.flow.Flow

interface GetRadioUseCase {
  suspend fun getRadio(radioId: Long): Flow<RadioDomainModel>
}