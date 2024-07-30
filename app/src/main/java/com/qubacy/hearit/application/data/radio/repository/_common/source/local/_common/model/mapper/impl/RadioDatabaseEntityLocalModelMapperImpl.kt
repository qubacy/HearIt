package com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.mapper.impl

import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.dao.entity.RadioDatabaseEntity
import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.RadioLocalModel
import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.mapper._common.RadioDatabaseEntityLocalModelMapper
import javax.inject.Inject

class RadioDatabaseEntityLocalModelMapperImpl @Inject constructor(

) : RadioDatabaseEntityLocalModelMapper {
    override fun map(radioDatabaseEntity: RadioDatabaseEntity): RadioLocalModel {
        return RadioLocalModel(
            radioDatabaseEntity.id,
            radioDatabaseEntity.title,
            radioDatabaseEntity.description,
            radioDatabaseEntity.coverUri,
            radioDatabaseEntity.url
        )
    }

    override fun map(radioLocalModel: RadioLocalModel): RadioDatabaseEntity {
        return RadioDatabaseEntity(
            radioLocalModel.id,
            radioLocalModel.title,
            radioLocalModel.description,
            radioLocalModel.coverUri,
            radioLocalModel.url
        )
    }
}