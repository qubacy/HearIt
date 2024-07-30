package com.qubacy.hearit.application.domain._common.model.mapper._di.module

import com.qubacy.hearit.application.domain._common.model.mapper._common.RadioDomainModelDataModelMapper
import com.qubacy.hearit.application.domain._common.model.mapper.impl.RadioDomainModelDataModelMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RadioDomainModelDataModelMapperModule {
    @Binds
    fun bindRadioDomainModelDataModelMapper(
        radioDomainModelDataModelMapper: RadioDomainModelDataModelMapperImpl
    ): RadioDomainModelDataModelMapper
}