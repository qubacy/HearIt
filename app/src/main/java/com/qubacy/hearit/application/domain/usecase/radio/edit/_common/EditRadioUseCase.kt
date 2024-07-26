package com.qubacy.hearit.application.domain.usecase.radio.edit._common

import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.RadioDomainSketch
import kotlinx.coroutines.flow.Flow

interface EditRadioUseCase {
    // todo: is it alright or it'd be better to pass RadioDomainModel instead?:
    suspend fun saveRadio(radioId: Long, radio: RadioDomainSketch): Flow<RadioDomainModel>
}