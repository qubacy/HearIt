package com.qubacy.hearit.application.ui.state.state.home.player.mapper.player.impl

import com.qubacy.hearit.application._common.player.packet.PlayerStatePacketBody
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.state.state.home.player.PlayerState
import com.qubacy.hearit.application.ui.state.state.home.player.mapper.player._common.PlayerStateStatePacketBodyMapper
import javax.inject.Inject

class PlayerStateStatePacketBodyMapperImpl @Inject constructor(

) : PlayerStateStatePacketBodyMapper {
  override fun map(
    radioPresentation: RadioPresentation,
    playerStatePacketBody: PlayerStatePacketBody,
  ): PlayerState {
    return PlayerState(radioPresentation, playerStatePacketBody.isPlaying)
  }

  override fun map(playerState: PlayerState, newRadioId: Long?): PlayerStatePacketBody {
    val radioId = newRadioId ?: playerState.currentRadio?.id

    return PlayerStatePacketBody(radioId, playerState.isRadioPlaying)
  }
}