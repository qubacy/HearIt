package com.qubacy.hearit.application.data.radio.repository._common.source.local._common.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.dao.entity.RadioDatabaseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RadioLocalDatabaseSourceDao {
  @Query(
    "SELECT * FROM ${RadioDatabaseEntity.TABLE_NAME} " +
    "WHERE ${RadioDatabaseEntity.ID_PROP_NAME} = :id"
  )
  fun getRadio(id: Long): Flow<RadioDatabaseEntity>

  @Query("SELECT * FROM ${RadioDatabaseEntity.TABLE_NAME}")
  fun allRadios(): Flow<List<RadioDatabaseEntity>>

  @Insert
  suspend fun insertRadio(radio: RadioDatabaseEntity): Long

  @Update
  suspend fun updateRadio(radio: RadioDatabaseEntity)
}