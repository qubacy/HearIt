package com.qubacy.hearit.application.ui.state.state.home.player.mapper._common

import com.qubacy.hearit.application.data.player.model.PlayerInfoDataModel
import com.qubacy.hearit.application.ui.state.state.home.player.PlayerState

interface PlayerStateInfoDataModelMapper {
  fun map(playerState: PlayerState): PlayerInfoDataModel
  fun map(playerInfoDataModel: PlayerInfoDataModel): PlayerState
}