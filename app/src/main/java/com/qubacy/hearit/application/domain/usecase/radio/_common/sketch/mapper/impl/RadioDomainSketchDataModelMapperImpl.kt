package com.qubacy.hearit.application.domain.usecase.radio._common.sketch.mapper.impl

import com.qubacy.hearit.application.data.radio.model.RadioDataModel
import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.RadioDomainSketch
import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.mapper._common.RadioDomainSketchDataModelMapper

class RadioDomainSketchDataModelMapperImpl : RadioDomainSketchDataModelMapper {
    override fun map(
        radioId: Long?,
        radioDomainSketch: RadioDomainSketch
    ): RadioDataModel {
        return RadioDataModel(
            radioId,
            radioDomainSketch.title,
            radioDomainSketch.description,
            radioDomainSketch.coverUri,
            radioDomainSketch.url
        )
    }
}