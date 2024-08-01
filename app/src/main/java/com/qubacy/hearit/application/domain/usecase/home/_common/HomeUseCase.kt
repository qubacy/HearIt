package com.qubacy.hearit.application.domain.usecase.home._common

import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import kotlinx.coroutines.flow.Flow

interface HomeUseCase {
    suspend fun getRadio(radioId: Long): Flow<RadioDomainModel>
    suspend fun getRadioList(): Flow<List<RadioDomainModel>>
}