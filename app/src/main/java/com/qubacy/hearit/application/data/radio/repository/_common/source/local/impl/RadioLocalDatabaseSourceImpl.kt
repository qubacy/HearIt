package com.qubacy.hearit.application.data.radio.repository._common.source.local.impl

import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.RadioLocalDatabaseSource
import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.dao.RadioLocalDatabaseSourceDao
import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.RadioLocalModel
import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.mapper._common.RadioDatabaseEntityLocalModelMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RadioLocalDatabaseSourceImpl @Inject constructor(
  private val _dao: RadioLocalDatabaseSourceDao,
  private val _mapper: RadioDatabaseEntityLocalModelMapper
) : RadioLocalDatabaseSource {
  override fun getAllRadios(): Flow<RadioLocalModel> {
    return _dao.allRadios().map { _mapper.map(it) }
  }

  override suspend fun addRadio(radioLocalModel: RadioLocalModel) {
    return _dao.insertRadio(_mapper.map(radioLocalModel))
  }

  override suspend fun updateRadio(radioLocalModel: RadioLocalModel) {
    return _dao.updateRadio(_mapper.map(radioLocalModel))
  }
}