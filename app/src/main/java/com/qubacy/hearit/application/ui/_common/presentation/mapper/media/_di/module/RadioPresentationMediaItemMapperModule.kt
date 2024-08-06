package com.qubacy.hearit.application.ui._common.presentation.mapper.media._di.module

import com.qubacy.hearit.application.ui._common.presentation.mapper.media._common.RadioPresentationMediaItemMapper
import com.qubacy.hearit.application.ui._common.presentation.mapper.media.impl.RadioPresentationMediaItemMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RadioPresentationMediaItemMapperModule {
  @Binds
  fun bindRadioPresentationMediaItemMapper(
    radioPresentationMediaItemMapper: RadioPresentationMediaItemMapperImpl
  ): RadioPresentationMediaItemMapper
}