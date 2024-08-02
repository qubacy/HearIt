package com.qubacy.hearit.application.data.player.repository._common.source.local._common.model.mapper._di.module

import com.qubacy.hearit.application.data.player.repository._common.source.local._common.model.mapper._common.PlayerInfoDataStorePreferenceMapper
import com.qubacy.hearit.application.data.player.repository._common.source.local._common.model.mapper.impl.PlayerInfoDataStorePreferenceMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface PlayerInfoDataStorePreferenceMapperModule {
  @Binds
  fun bindPlayerInfoDataStorePreferenceMapper(
    playerInfoDataStorePreferenceMapper: PlayerInfoDataStorePreferenceMapperImpl
  ): PlayerInfoDataStorePreferenceMapper
}