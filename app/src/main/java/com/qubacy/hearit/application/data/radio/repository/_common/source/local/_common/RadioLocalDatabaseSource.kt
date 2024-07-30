package com.qubacy.hearit.application.data.radio.repository._common.source.local._common

import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.RadioLocalModel
import kotlinx.coroutines.flow.Flow

interface RadioLocalDatabaseSource {
  fun getAllRadios(): Flow<RadioLocalModel>
  suspend fun addRadio(radioLocalModel: RadioLocalModel)
  suspend fun updateRadio(radioLocalModel: RadioLocalModel)
}