package com.qubacy.hearit.application.ui.state.state.home.player.mapper.player._common

import com.qubacy.hearit.application._common.player.packet.PlayerStatePacketBody
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.state.state.home.player.PlayerState

interface PlayerStateStatePacketBodyMapper {
  fun map(
    radioPresentation: RadioPresentation,
    playerStatePacketBody: PlayerStatePacketBody
  ): PlayerState
  fun map(playerState: PlayerState): PlayerStatePacketBody
}