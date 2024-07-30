package com.qubacy.hearit.application.domain.usecase.radio.add._common

import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.RadioDomainSketch

interface AddRadioUseCase {
    suspend fun addRadio(radio: RadioDomainSketch): Long
}