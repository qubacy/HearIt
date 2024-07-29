package com.qubacy.hearit.application.data.radio.repository.source.local.impl

import com.qubacy.hearit.application.data.radio.repository.source.local._common.RadioLocalDatabaseSource
import com.qubacy.hearit.application.data.radio.repository.source.local._common.dao.RadioLocalDatabaseSourceDao
import com.qubacy.hearit.application.data.radio.repository.source.local._common.model.RadioLocalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RadioLocalDatabaseSourceImpl @Inject constructor(
  private val _dao: RadioLocalDatabaseSourceDao
) : RadioLocalDatabaseSource {
  override fun getAllRadios(): Flow<RadioLocalModel> {
    return _dao.allRadios().map { RadioLocalModel.fromRadioDatabaseEntity(it) }
  }

  override suspend fun addRadio(radioLocalModel: RadioLocalModel) {
    return _dao.insertRadio(radioLocalModel.toRadioDatabaseEntity())
  }

  override suspend fun updateRadio(radioLocalModel: RadioLocalModel) {
    return _dao.updateRadio(radioLocalModel.toRadioDatabaseEntity())
  }
}