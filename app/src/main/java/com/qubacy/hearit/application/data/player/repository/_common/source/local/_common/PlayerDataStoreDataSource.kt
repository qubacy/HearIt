package com.qubacy.hearit.application.data.player.repository._common.source.local._common

import com.qubacy.hearit.application.data.player.repository._common.source.local._common.model.PlayerInfoDataStoreModel
import kotlinx.coroutines.flow.Flow

interface PlayerDataStoreDataSource {
  fun getPlayerInfo(): Flow<PlayerInfoDataStoreModel>
  suspend fun updatePlayerInfo(playerInfo: PlayerInfoDataStoreModel)
}