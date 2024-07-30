package com.qubacy.hearit.application.data.radio.model.mapper.impl

import com.qubacy.hearit.application.data.radio.model.RadioDataModel
import com.qubacy.hearit.application.data.radio.model.mapper._common.RadioLocalModelDataModelMapper
import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.RadioLocalModel

class RadioLocalModelDataModelMapperImpl : RadioLocalModelDataModelMapper {
    override fun map(radioLocalModel: RadioLocalModel): RadioDataModel {
        return RadioDataModel(
            radioLocalModel.id,
            radioLocalModel.title,
            radioLocalModel.description,
            radioLocalModel.coverUri,
            radioLocalModel.url
        )
    }
}