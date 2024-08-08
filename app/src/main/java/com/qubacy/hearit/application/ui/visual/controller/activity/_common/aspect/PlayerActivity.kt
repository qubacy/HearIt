package com.qubacy.hearit.application.ui.visual.controller.activity._common.aspect

import com.qubacy.hearit.application._common.player.packet.PlayerStatePacketBody

interface PlayerActivity {
  interface Callback {
    fun onPlayerStatePacketGotten(playerStatePacketBody: PlayerStatePacketBody)
  }

  fun setPlayerActivityCallback(callback: Callback)
  fun setPlayerState(playerStatePacketBody: PlayerStatePacketBody)

//  fun getPlayer(): Player
}