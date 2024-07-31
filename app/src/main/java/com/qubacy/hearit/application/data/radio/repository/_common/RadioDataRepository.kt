package com.qubacy.hearit.application.data.radio.repository._common

import com.qubacy.hearit.application.data.radio.model.RadioDataModel
import kotlinx.coroutines.flow.Flow

interface RadioDataRepository {
  suspend fun getRadio(id: Long): Flow<RadioDataModel>
  suspend fun getAllRadios(): Flow<List<RadioDataModel>>
  suspend fun addRadio(radioDataModel: RadioDataModel): Long
  suspend fun updateRadio(radioDataModel: RadioDataModel)
}