package com.qubacy.hearit.application.data.radio.repository.source.local._common.dao._di.module

import com.qubacy.hearit.application.data._common.repository.source.local.database.HearItDatabase
import com.qubacy.hearit.application.data.radio.repository.source.local._common.dao.RadioLocalDatabaseSourceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RadioLocalDatabaseSourceDaoModule {
  @Provides
  fun provideRadioLocalDatabaseSourceDao(
    database: HearItDatabase
  ): RadioLocalDatabaseSourceDao {
    return database.radioDao()
  }
}