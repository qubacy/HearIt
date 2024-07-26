package com.qubacy.hearit.application.domain.usecase.home._di.module

import com.qubacy.hearit.application.domain.usecase.home._common.HomeUseCase
import com.qubacy.hearit.application.domain.usecase.home.impl.HomeUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface HomeUseCaseModule {
  @Binds
  fun bindHomeUseCase(homeUseCase: HomeUseCaseImpl): HomeUseCase
}