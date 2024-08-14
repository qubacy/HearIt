package com.qubacy.hearit.application._common.player.bus._common

import com.qubacy.hearit.application._common.player.packet.PlayerStatePacket
import kotlinx.coroutines.flow.Flow

interface PlayerStatePacketBus {
  val playerStatePacket: Flow<PlayerStatePacket>

  suspend fun postPlayerStatePacket(packet: PlayerStatePacket)
}