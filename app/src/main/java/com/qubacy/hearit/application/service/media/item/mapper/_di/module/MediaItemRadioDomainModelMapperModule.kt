package com.qubacy.hearit.application.service.media.item.mapper._di.module

import com.qubacy.hearit.application.service.media.item.mapper._common.MediaItemRadioDomainModelMapper
import com.qubacy.hearit.application.service.media.item.mapper.impl.MediaItemRadioDomainModelMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface MediaItemRadioDomainModelMapperModule {
  @Binds
  fun bindMediaItemRadioDomainModelMapper(
    mediaItemRadioDomainModelMapper: MediaItemRadioDomainModelMapperImpl
  ): MediaItemRadioDomainModelMapper
}