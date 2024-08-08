package com.qubacy.hearit.application._common.player.bus._common

import com.qubacy.hearit.application._common.player.packet.PlayerStatePacket
import kotlinx.coroutines.flow.StateFlow

interface PlayerStatePacketBus {
  val playerStatePacket: StateFlow<PlayerStatePacket>

  fun postPlayerStatePacket(packet: PlayerStatePacket)
}