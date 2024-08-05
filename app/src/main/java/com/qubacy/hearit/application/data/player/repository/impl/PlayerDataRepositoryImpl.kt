package com.qubacy.hearit.application.data.player.repository.impl

import com.qubacy.hearit.application.data.player.model.PlayerInfoDataModel
import com.qubacy.hearit.application.data.player.model.mapper._common.PlayerInfoDataModelDataStoreModelMapper
import com.qubacy.hearit.application.data.player.repository._common.PlayerDataRepository
import com.qubacy.hearit.application.data.player.repository._common.source.local._common.PlayerDataStoreDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayerDataRepositoryImpl @Inject constructor(
  private val _localSource: PlayerDataStoreDataSource,
  private val _mapper: PlayerInfoDataModelDataStoreModelMapper
) : PlayerDataRepository {
  override fun getPlayerInfo(): Flow<PlayerInfoDataModel> {
    return _localSource.getPlayerInfo().map { _mapper.map(it) }
  }

  override suspend fun updatePlayerInfo(playerInfo: PlayerInfoDataModel) {
    _localSource.updatePlayerInfo(_mapper.map(playerInfo))
  }
}