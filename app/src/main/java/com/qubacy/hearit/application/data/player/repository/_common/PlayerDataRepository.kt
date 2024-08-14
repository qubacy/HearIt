package com.qubacy.hearit.application.data.player.repository._common

import com.qubacy.hearit.application.data.player.model.PlayerInfoDataModel
import kotlinx.coroutines.flow.Flow

@Deprecated("There's no need to use it now. The state is handled by the bus.")
interface PlayerDataRepository {
  fun getPlayerInfo(): Flow<PlayerInfoDataModel>
  suspend fun updatePlayerInfo(playerInfo: PlayerInfoDataModel)
}