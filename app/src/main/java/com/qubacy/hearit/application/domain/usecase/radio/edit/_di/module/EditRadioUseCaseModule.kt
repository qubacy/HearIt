package com.qubacy.hearit.application.domain.usecase.radio.edit._di.module

import com.qubacy.hearit.application.domain.usecase.radio.edit._common.EditRadioUseCase
import com.qubacy.hearit.application.domain.usecase.radio.edit.impl.EditRadioUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface EditRadioUseCaseModule {
  @Binds
  fun bindEditRadioUseCase(editRadioUseCase: EditRadioUseCaseImpl): EditRadioUseCase
}