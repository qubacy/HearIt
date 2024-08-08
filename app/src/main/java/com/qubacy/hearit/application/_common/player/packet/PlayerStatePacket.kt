package com.qubacy.hearit.application._common.player.packet

data class PlayerStatePacket(
  val body: PlayerStatePacketBody = PlayerStatePacketBody(),
  val senderId: String = DEFAULT_SENDER
) {
  companion object {
    const val DEFAULT_SENDER = "defaultSender"
  }
}