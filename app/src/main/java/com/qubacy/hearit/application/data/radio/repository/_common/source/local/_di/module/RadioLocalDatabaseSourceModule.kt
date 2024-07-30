package com.qubacy.hearit.application.data.radio.repository._common.source.local._di.module

import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.RadioLocalDatabaseSource
import com.qubacy.hearit.application.data.radio.repository._common.source.local.impl.RadioLocalDatabaseSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RadioLocalDatabaseSourceModule {
  @Binds
  fun bindRadioLocalDatabaseSource(
    radioLocalDatabaseSource: RadioLocalDatabaseSourceImpl
  ): RadioLocalDatabaseSource
}