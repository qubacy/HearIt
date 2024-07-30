package com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.mapper._common

import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.dao.entity.RadioDatabaseEntity
import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.RadioLocalModel

interface RadioDatabaseEntityLocalModelMapper {
    fun map(radioDatabaseEntity: RadioDatabaseEntity): RadioLocalModel
    fun map(radioLocalModel: RadioLocalModel): RadioDatabaseEntity
}