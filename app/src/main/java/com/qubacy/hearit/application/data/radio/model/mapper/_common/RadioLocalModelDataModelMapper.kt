package com.qubacy.hearit.application.data.radio.model.mapper._common

import com.qubacy.hearit.application.data.radio.model.RadioDataModel
import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.RadioLocalModel

interface RadioLocalModelDataModelMapper {
    fun map(radioLocalModel: RadioLocalModel): RadioDataModel
}