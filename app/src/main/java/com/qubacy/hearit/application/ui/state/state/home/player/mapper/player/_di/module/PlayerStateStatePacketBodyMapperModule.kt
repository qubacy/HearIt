package com.qubacy.hearit.application.ui.state.state.home.player.mapper.player._di.module

import com.qubacy.hearit.application.ui.state.state.home.player.mapper.player._common.PlayerStateStatePacketBodyMapper
import com.qubacy.hearit.application.ui.state.state.home.player.mapper.player.impl.PlayerStateStatePacketBodyMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface PlayerStateStatePacketBodyMapperModule {
  @Binds
  fun bindPlayerStateStatePacketBodyMapper(
    playerStateStatePacketBodyMapper: PlayerStateStatePacketBodyMapperImpl
  ): PlayerStateStatePacketBodyMapper
}