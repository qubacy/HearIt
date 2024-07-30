package com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.mapper._di.module

import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.mapper._common.RadioDatabaseEntityLocalModelMapper
import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.mapper.impl.RadioDatabaseEntityLocalModelMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RadioDatabaseEntityLocalModelMapperModule {
    @Binds
    fun bindRadioDatabaseEntityLocalModelMapper(
        radioDatabaseEntityLocalModelMapper: RadioDatabaseEntityLocalModelMapperImpl
    ): RadioDatabaseEntityLocalModelMapper
}