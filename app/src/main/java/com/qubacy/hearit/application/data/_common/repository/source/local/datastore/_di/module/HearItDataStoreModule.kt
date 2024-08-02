package com.qubacy.hearit.application.data._common.repository.source.local.datastore._di.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.qubacy.hearit.application.data._common.repository.source.local.datastore.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HearItDataStoreModule {
  @Provides
  fun provideHearItDataStore(
    @ApplicationContext context: Context
  ): DataStore<Preferences> {
    return context.dataStore
  }
}