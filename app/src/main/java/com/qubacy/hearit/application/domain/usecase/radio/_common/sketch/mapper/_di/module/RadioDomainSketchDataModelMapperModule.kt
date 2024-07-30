package com.qubacy.hearit.application.domain.usecase.radio._common.sketch.mapper._di.module

import com.qubacy.hearit.application.domain._common.model.mapper.impl.RadioDomainModelDataModelMapperImpl
import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.mapper._common.RadioDomainSketchDataModelMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RadioDomainSketchDataModelMapperModule {
    @Binds
    fun bindRadioDomainSketchDataModelMapper(
        radioDomainModelDataModelMapper: RadioDomainModelDataModelMapperImpl
    ): RadioDomainSketchDataModelMapper
}