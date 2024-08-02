package com.qubacy.hearit.application.data.player.repository._common.source.local._common.model

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey

data class PlayerInfoDataStoreModel(
  val curRadioId: Long = UNKNOWN_RADIO_ID,
  val isPlaying: Boolean = false
) {
  companion object {
    val CUR_RADIO_ID_PREFERENCE_KEY = longPreferencesKey("curRadioId")
    val IS_PLAYING_PREFERENCE_KEY = booleanPreferencesKey("isPlaying")

    const val UNKNOWN_RADIO_ID = -1L
  }
}
