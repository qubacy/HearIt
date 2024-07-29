package com.qubacy.hearit.application.data._common.repository.source.local.database._di.module

import android.content.Context
import androidx.room.Room
import com.qubacy.hearit.application.data._common.repository.source.local.database.HearItDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeartItDatabaseModule {
  @Provides
  @Singleton
  fun provideHeartItDatabase(
    @ApplicationContext context: Context
  ): HearItDatabase {
    return Room.databaseBuilder(
      context, HearItDatabase::class.java, HearItDatabase.NAME
    ).fallbackToDestructiveMigration().build()
  }
}