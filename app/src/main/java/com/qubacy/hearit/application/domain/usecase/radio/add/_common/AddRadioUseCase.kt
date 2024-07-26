package com.qubacy.hearit.application.domain.usecase.radio.add._common

import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.RadioDomainSketch
import kotlinx.coroutines.flow.Flow

interface AddRadioUseCase {
    suspend fun addRadio(radio: RadioDomainSketch): Flow<RadioDomainModel>
}