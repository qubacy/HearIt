package com.qubacy.hearit.application.ui.state.state.home.player.mapper.data._di.module

import com.qubacy.hearit.application.ui.state.state.home.player.mapper.data._common.PlayerStateInfoDataModelMapper
import com.qubacy.hearit.application.ui.state.state.home.player.mapper.data.impl.PlayerStateInfoDataModelMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface PlayerStateInfoDataModelMapperModule {
  @Binds
  fun bindPlayerStateInfoDataModelMapper(
    playerStateInfoDataModelMapper: PlayerStateInfoDataModelMapperImpl
  ): PlayerStateInfoDataModelMapper
}