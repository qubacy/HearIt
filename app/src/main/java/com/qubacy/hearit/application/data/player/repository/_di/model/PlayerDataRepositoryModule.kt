package com.qubacy.hearit.application.data.player.repository._di.model

import com.qubacy.hearit.application.data.player.repository._common.PlayerDataRepository
import com.qubacy.hearit.application.data.player.repository.impl.PlayerDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface PlayerDataRepositoryModule {
  @Binds
  fun bindPlayerDataRepository(playerDataRepository: PlayerDataRepositoryImpl): PlayerDataRepository
}