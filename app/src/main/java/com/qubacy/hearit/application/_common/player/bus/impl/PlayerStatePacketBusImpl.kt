package com.qubacy.hearit.application._common.player.bus.impl

import com.qubacy.hearit.application._common.player.bus._common.PlayerStatePacketBus
import com.qubacy.hearit.application._common.player.packet.PlayerStatePacket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class PlayerStatePacketBusImpl @Inject constructor() : PlayerStatePacketBus {
  private var _playerStatePacket = MutableStateFlow(PlayerStatePacket())
  override val playerStatePacket: StateFlow<PlayerStatePacket> get() = _playerStatePacket

  override fun postPlayerStatePacket(packet: PlayerStatePacket) {
    _playerStatePacket.value = packet
  }
}