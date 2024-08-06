package com.qubacy.hearit.application.ui._common.presentation.mapper.domain._di.module

import com.qubacy.hearit.application.ui._common.presentation.mapper.domain._common.RadioPresentationDomainModelMapper
import com.qubacy.hearit.application.ui._common.presentation.mapper.domain.impl.RadioPresentationDomainModelMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RadioPresentationDomainModelMapperModule {
  @Binds
  fun bindRadioPresentationDomainModelMapper(
    radioPresentationDomainModelMapper: RadioPresentationDomainModelMapperImpl
  ): RadioPresentationDomainModelMapper
}