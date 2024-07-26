package com.qubacy.hearit.application.domain.usecase.radio.add._di.module

import com.qubacy.hearit.application.domain.usecase.radio.add._common.AddRadioUseCase
import com.qubacy.hearit.application.domain.usecase.radio.add.impl.AddRadioUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AddRadioUseCaseModule {
  @Binds
  fun bindAddRadioUseCase(addRadioUseCase: AddRadioUseCaseImpl): AddRadioUseCase
}