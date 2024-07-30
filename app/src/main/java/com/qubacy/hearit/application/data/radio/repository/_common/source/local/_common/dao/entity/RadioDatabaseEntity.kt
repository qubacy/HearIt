package com.qubacy.hearit.application.data.radio.repository._common.source.local._common.dao.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = RadioDatabaseEntity.TABLE_NAME)
data class RadioDatabaseEntity(
  @PrimaryKey(autoGenerate = true) val id: Long?,
  val title: String,
  val description: String?,
  @ColumnInfo(name = "cover_uri") val coverUri: String?,
  val url: String
) {
  companion object {
    const val TABLE_NAME = "Radio"
  }
}