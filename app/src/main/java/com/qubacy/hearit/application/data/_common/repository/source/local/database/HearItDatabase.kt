package com.qubacy.hearit.application.data._common.repository.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.qubacy.hearit.application.data.radio.repository.source.local._common.dao.RadioLocalDatabaseSourceDao
import com.qubacy.hearit.application.data.radio.repository.source.local._common.dao.entity.RadioDatabaseEntity

@Database(
  entities = [RadioDatabaseEntity::class],
  version = 1
)
abstract class HearItDatabase : RoomDatabase() {
  companion object {
    const val NAME = "hearit.db"
  }

  abstract fun radioDao(): RadioLocalDatabaseSourceDao
}