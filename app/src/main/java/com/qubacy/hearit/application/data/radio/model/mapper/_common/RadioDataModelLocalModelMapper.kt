package com.qubacy.hearit.application.data.radio.model.mapper._common

import com.qubacy.hearit.application.data.radio.model.RadioDataModel
import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.RadioLocalModel

interface RadioDataModelLocalModelMapper {
    fun map(radioLocalModel: RadioLocalModel): RadioDataModel
    fun map(radioDataModel: RadioDataModel): RadioLocalModel
}