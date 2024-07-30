package com.qubacy.hearit.application.data.radio.model.mapper._di.module

import com.qubacy.hearit.application.data.radio.model.mapper._common.RadioDataModelLocalModelMapper
import com.qubacy.hearit.application.data.radio.model.mapper.impl.RadioDataModelLocalModelMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RadioDataModelLocalModelMapperModule {
    @Binds
    fun bindRadioDataModelLocalModelMapper(
        radioLocalModelDataModelMapper: RadioDataModelLocalModelMapperImpl
    ): RadioDataModelLocalModelMapper
}