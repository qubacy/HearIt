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
  companion object {
    const val TAG = "RadioLocalDatabaseSourceImpl"
  }

  override fun getRadio(id: Long): Flow<RadioLocalModel> {
    return _dao.getRadio(id).map { _mapper.map(it) }
  }

  override fun getAllRadios(): Flow<List<RadioLocalModel>> {
    return _dao.allRadios().map { list -> list.map { _mapper.map(it) } }
  }

  override suspend fun addRadio(radioLocalModel: RadioLocalModel): Long {
    return _dao.insertRadio(_mapper.map(radioLocalModel))
  }

  override suspend fun updateRadio(radioLocalModel: RadioLocalModel) {
    return _dao.updateRadio(_mapper.map(radioLocalModel))
  }
}