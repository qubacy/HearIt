package com.qubacy.hearit.application.data.radio.repository.impl

import com.qubacy.hearit.application.data.radio.model.RadioDataModel
import com.qubacy.hearit.application.data.radio.model.mapper._common.RadioDataModelLocalModelMapper
import com.qubacy.hearit.application.data.radio.repository._common.RadioDataRepository
import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.RadioLocalDatabaseSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RadioDataRepositoryImpl @Inject constructor(
  private val _localSource: RadioLocalDatabaseSource,
  private val _mapper: RadioDataModelLocalModelMapper
) : RadioDataRepository {
  companion object {
    const val TAG = "RadioDataRepositoryImpl"
  }

  override suspend fun getRadio(id: Long): Flow<RadioDataModel> {
    return _localSource.getRadio(id).map { _mapper.map(it) }
  }

  override suspend fun getAllRadios(): Flow<List<RadioDataModel>> {
    return _localSource.getAllRadios().map { list -> list.map { _mapper.map(it) } }
  }

  override suspend fun addRadio(radioDataModel: RadioDataModel): Long {
    return _localSource.addRadio(_mapper.map(radioDataModel))
  }

  override suspend fun updateRadio(radioDataModel: RadioDataModel) {
    return _localSource.updateRadio(_mapper.map(radioDataModel))
  }
}