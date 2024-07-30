package com.qubacy.hearit.application.data.radio.model.mapper.impl

import com.qubacy.hearit.application.data.radio.model.RadioDataModel
import com.qubacy.hearit.application.data.radio.model.mapper._common.RadioDataModelLocalModelMapper
import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.RadioLocalModel
import javax.inject.Inject

class RadioDataModelLocalModelMapperImpl @Inject constructor(

) : RadioDataModelLocalModelMapper {
    override fun map(radioLocalModel: RadioLocalModel): RadioDataModel {
        return RadioDataModel(
            radioLocalModel.id,
            radioLocalModel.title,
            radioLocalModel.description,
            radioLocalModel.coverUri,
            radioLocalModel.url
        )
    }

    override fun map(radioDataModel: RadioDataModel): RadioLocalModel {
        return RadioLocalModel(
            radioDataModel.id,
            radioDataModel.title,
            radioDataModel.description,
            radioDataModel.cover,
            radioDataModel.url
        )
    }
}