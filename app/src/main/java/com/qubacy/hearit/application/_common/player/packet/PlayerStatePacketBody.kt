package com.qubacy.hearit.application._common.player.packet

data class PlayerStatePacketBody(
  val radioId: Long? = null,
  val isPlaying: Boolean = false,
) {

}