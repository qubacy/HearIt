package com.qubacy.hearit.application.domain._common.model.mapper.impl

import com.qubacy.hearit.application.data.radio.model.RadioDataModel
import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.domain._common.model.mapper._common.RadioDomainModelDataModelMapper

class RadioDomainModelDataModelMapperImpl : RadioDomainModelDataModelMapper {
    override fun map(radioDataModel: RadioDataModel): RadioDomainModel {
        assert(radioDataModel.id != null) {
            "RadioDataModel.id should be not null during the mapping to RadioDomainModel;"
        }

        return RadioDomainModel(
            radioDataModel.id!!,
            radioDataModel.title,
            radioDataModel.description,
            radioDataModel.cover,
            radioDataModel.url
        )
    }
}