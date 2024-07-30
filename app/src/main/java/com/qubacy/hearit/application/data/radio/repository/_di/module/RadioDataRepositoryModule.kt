package com.qubacy.hearit.application.data.radio.repository._di.module

import com.qubacy.hearit.application.data.radio.repository._common.RadioDataRepository
import com.qubacy.hearit.application.data.radio.repository.impl.RadioDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RadioDataRepositoryModule {
  @Binds
  fun bindRadioDataRepository(radioDataRepository: RadioDataRepositoryImpl): RadioDataRepository
}