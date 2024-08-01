package com.qubacy.hearit.application.domain.usecase.radio.get._di.module

import com.qubacy.hearit.application.domain.usecase.radio.get._common.GetRadioUseCase
import com.qubacy.hearit.application.domain.usecase.radio.get.impl.GetRadioUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface GetRadioUseCaseModule {
  @Binds
  fun bindGetRadioUseCase(getRadioUseCase: GetRadioUseCaseImpl): GetRadioUseCase
}