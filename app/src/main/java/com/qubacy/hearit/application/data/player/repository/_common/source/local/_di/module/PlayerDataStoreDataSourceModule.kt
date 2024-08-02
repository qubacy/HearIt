package com.qubacy.hearit.application.data.player.repository._common.source.local._di.module

import com.qubacy.hearit.application.data.player.repository._common.source.local._common.PlayerDataStoreDataSource
import com.qubacy.hearit.application.data.player.repository._common.source.local.impl.PlayerDataStoreDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface PlayerDataStoreDataSourceModule {
  @Binds
  fun bindPlayerDataStoreDataSource(
    playerDataStoreDataSource: PlayerDataStoreDataSourceImpl
  ): PlayerDataStoreDataSource
}