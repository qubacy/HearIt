package com.qubacy.hearit.application.data.radio.model.mapper._di.module

import com.qubacy.hearit.application.data.radio.model.mapper._common.RadioLocalModelDataModelMapper
import com.qubacy.hearit.application.data.radio.model.mapper.impl.RadioLocalModelDataModelMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RadioLocalModelDataModelMapperModule {
    @Binds
    fun bindRadioLocalModelDataModelMapper(
        radioLocalModelDataModelMapper: RadioLocalModelDataModelMapperImpl
    ): RadioLocalModelDataModelMapper
}