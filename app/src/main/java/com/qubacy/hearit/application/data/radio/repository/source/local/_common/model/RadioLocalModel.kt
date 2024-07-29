package com.qubacy.hearit.application.data.radio.repository.source.local._common.model

import com.qubacy.hearit.application.data.radio.repository.source.local._common.dao.entity.RadioDatabaseEntity

data class RadioLocalModel(
  val id: Long,
  val title: String,
  val description: String?,
  val coverUri: String?,
  val url: String
) {
  companion object {
    fun fromRadioDatabaseEntity(radioDatabaseEntity: RadioDatabaseEntity): RadioLocalModel {
      return RadioLocalModel(
        radioDatabaseEntity.id,
        radioDatabaseEntity.title,
        radioDatabaseEntity.description,
        radioDatabaseEntity.coverUri,
        radioDatabaseEntity.url
      )
    }
  }

  fun toRadioDatabaseEntity(): RadioDatabaseEntity {
    return RadioDatabaseEntity(id, title, description, coverUri, url)
  }
}