package com.qubacy.hearit.application.data.player.repository._common.source.local.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.qubacy.hearit.application.data.player.repository._common.source.local._common.PlayerDataStoreDataSource
import com.qubacy.hearit.application.data.player.repository._common.source.local._common.model.PlayerInfoDataStoreModel
import com.qubacy.hearit.application.data.player.repository._common.source.local._common.model.mapper._common.PlayerInfoDataStorePreferenceMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlayerDataStoreDataSourceImpl @Inject constructor(
  private val _dataStore: DataStore<Preferences>,
  private val _mapper: PlayerInfoDataStorePreferenceMapper
) : PlayerDataStoreDataSource {
  override fun getPlayerInfo(): Flow<PlayerInfoDataStoreModel> {
    return _dataStore.data.map { preferences -> _mapper.map(preferences) }
  }

  override suspend fun updatePlayerInfo(playerInfo: PlayerInfoDataStoreModel) {
    _dataStore.edit { preferencies -> _mapper.map(preferencies, playerInfo) }
  }
}