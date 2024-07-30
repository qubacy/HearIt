package com.qubacy.hearit.application.domain._common.model.mapper._common

import com.qubacy.hearit.application.data.radio.model.RadioDataModel
import com.qubacy.hearit.application.domain._common.model.RadioDomainModel

interface RadioDomainModelDataModelMapper {
    fun map(radioDataModel: RadioDataModel): RadioDomainModel
}