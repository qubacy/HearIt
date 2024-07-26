package com.qubacy.hearit.application.ui.state.holder._common.dispatcher._di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ViewModelDispatcherModule {
  @Singleton
  @ViewModelDispatcherQualifier
  @Provides
  fun provideViewModelDispatcher(): CoroutineDispatcher {
    return Dispatchers.Default
  }
}