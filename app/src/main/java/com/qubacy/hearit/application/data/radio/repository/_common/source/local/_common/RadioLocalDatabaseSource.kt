package com.qubacy.hearit.application.data.radio.repository._common.source.local._common

import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.RadioLocalModel
import kotlinx.coroutines.flow.Flow

interface RadioLocalDatabaseSource {
  fun getRadio(id: Long): Flow<RadioLocalModel>
  fun getAllRadios(): Flow<List<RadioLocalModel>>
  suspend fun addRadio(radioLocalModel: RadioLocalModel): Long
  suspend fun updateRadio(radioLocalModel: RadioLocalModel)
}