package com.qubacy.hearit.application.data.player.model.mapper._di.module

import com.qubacy.hearit.application.data.player.model.mapper._common.PlayerInfoDataModelDataStoreModelMapper
import com.qubacy.hearit.application.data.player.model.mapper.impl.PlayerInfoDataModelDataStoreModelMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface PlayerInfoDataModelDataStoreModelMapperModule {
  @Binds
  fun bindPlayerInfoDataModelDataStoreModelMapper(
    playerInfoDataModelDataStoreModelMapper: PlayerInfoDataModelDataStoreModelMapperImpl
  ): PlayerInfoDataModelDataStoreModelMapper
}