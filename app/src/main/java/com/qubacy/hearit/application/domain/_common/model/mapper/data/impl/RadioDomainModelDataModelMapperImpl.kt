package com.qubacy.hearit.application.domain._common.model.mapper.data.impl

import com.qubacy.hearit.application.data.radio.model.RadioDataModel
import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.domain._common.model.mapper.data._common.RadioDomainModelDataModelMapper
import javax.inject.Inject

class RadioDomainModelDataModelMapperImpl @Inject constructor(

) : RadioDomainModelDataModelMapper {
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