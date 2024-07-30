package com.qubacy.hearit.application.domain.usecase.radio.edit._common

import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.RadioDomainSketch
import kotlinx.coroutines.flow.Flow

interface EditRadioUseCase {
    suspend fun getRadio(radioId: Long): Flow<RadioDomainModel>
    suspend fun saveRadio(radioId: Long, radio: RadioDomainSketch)
}