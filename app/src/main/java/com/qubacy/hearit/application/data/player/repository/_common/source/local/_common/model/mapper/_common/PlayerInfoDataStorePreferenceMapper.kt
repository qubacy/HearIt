package com.qubacy.hearit.application.data.player.repository._common.source.local._common.model.mapper._common

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import com.qubacy.hearit.application.data.player.repository._common.source.local._common.model.PlayerInfoDataStoreModel

interface PlayerInfoDataStorePreferenceMapper {
  fun map(preferences: Preferences): PlayerInfoDataStoreModel
  fun map(preferences: MutablePreferences, playerInfoDataStoreModel: PlayerInfoDataStoreModel)
}