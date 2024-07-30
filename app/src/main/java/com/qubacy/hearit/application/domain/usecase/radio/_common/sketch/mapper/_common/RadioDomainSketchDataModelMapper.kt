package com.qubacy.hearit.application.domain.usecase.radio._common.sketch.mapper._common

import com.qubacy.hearit.application.data.radio.model.RadioDataModel
import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.RadioDomainSketch

interface RadioDomainSketchDataModelMapper {
    fun map(radioId: Long?, radioDomainSketch: RadioDomainSketch): RadioDataModel
}