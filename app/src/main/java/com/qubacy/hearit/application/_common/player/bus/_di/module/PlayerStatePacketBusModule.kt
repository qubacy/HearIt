package com.qubacy.hearit.application._common.player.bus._di.module

import com.qubacy.hearit.application._common.player.bus._common.PlayerStatePacketBus
import com.qubacy.hearit.application._common.player.bus.impl.PlayerStatePacketBusImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PlayerStatePacketBusModule {
  @Binds
  @Singleton
  fun bindPlayerStatePacketBus(
    playerStatePacketBus: PlayerStatePacketBusImpl
  ): PlayerStatePacketBus
}