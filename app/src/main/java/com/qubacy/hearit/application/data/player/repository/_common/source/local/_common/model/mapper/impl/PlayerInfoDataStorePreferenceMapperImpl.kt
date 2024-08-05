package com.qubacy.hearit.application.data.player.repository._common.source.local._common.model.mapper.impl

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import com.qubacy.hearit.application.data.player.repository._common.source.local._common.model.PlayerInfoDataStoreModel
import com.qubacy.hearit.application.data.player.repository._common.source.local._common.model.mapper._common.PlayerInfoDataStorePreferenceMapper
import javax.inject.Inject

class PlayerInfoDataStorePreferenceMapperImpl @Inject constructor(

) : PlayerInfoDataStorePreferenceMapper {
  override fun map(preferences: Preferences): PlayerInfoDataStoreModel {
    return preferences.let {
      PlayerInfoDataStoreModel(
        it[PlayerInfoDataStoreModel.CUR_RADIO_ID_PREFERENCE_KEY]
          ?: PlayerInfoDataStoreModel.UNKNOWN_RADIO_ID,
        it[PlayerInfoDataStoreModel.IS_PLAYING_PREFERENCE_KEY] ?: false
      )
    }
  }

  override fun map(
    preferences: MutablePreferences,
    playerInfoDataStoreModel: PlayerInfoDataStoreModel,
  ) {
    preferences.apply {
      set(PlayerInfoDataStoreModel.CUR_RADIO_ID_PREFERENCE_KEY, playerInfoDataStoreModel.curRadioId)
      set(PlayerInfoDataStoreModel.IS_PLAYING_PREFERENCE_KEY, playerInfoDataStoreModel.isPlaying)
    }
  }
}