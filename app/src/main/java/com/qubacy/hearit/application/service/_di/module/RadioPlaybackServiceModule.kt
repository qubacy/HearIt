package com.qubacy.hearit.application.service._di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object RadioPlaybackServiceModule {
  @Provides
  @RadioPlaybackServiceCoroutineDispatcherQualifier
  fun provideRadioPlaybackServiceCoroutineDispatcher(): CoroutineDispatcher {
    return Dispatchers.Default
  }
}