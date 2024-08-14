package com.qubacy.hearit.application._common.player.bus.impl

import com.qubacy.hearit.application._common.player.bus._common.PlayerStatePacketBus
import com.qubacy.hearit.application._common.player.packet.PlayerStatePacket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class PlayerStatePacketBusImpl @Inject constructor() : PlayerStatePacketBus {
  private var _playerStatePacket = MutableStateFlow(PlayerStatePacket())
  override val playerStatePacket: Flow<PlayerStatePacket> get() = _playerStatePacket

  override suspend fun postPlayerStatePacket(packet: PlayerStatePacket) {
    _playerStatePacket.emit(packet)
  }
}