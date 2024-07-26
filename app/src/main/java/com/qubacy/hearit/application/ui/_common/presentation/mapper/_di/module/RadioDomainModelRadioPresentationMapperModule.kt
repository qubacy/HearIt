package com.qubacy.hearit.application.ui._common.presentation.mapper._di.module

import com.qubacy.hearit.application.ui._common.presentation.mapper._common.RadioDomainModelRadioPresentationMapper
import com.qubacy.hearit.application.ui._common.presentation.mapper.impl.RadioDomainModelRadioPresentationMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RadioDomainModelRadioPresentationMapperModule {
  @Binds
  fun bindRadioDomainModelRadioPresentationMapper(
    radioDomainModelRadioPresentationMapper: RadioDomainModelRadioPresentationMapperImpl
  ): RadioDomainModelRadioPresentationMapper
}