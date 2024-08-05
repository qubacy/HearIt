package com.qubacy.hearit.application.data.player.model.mapper._common

import com.qubacy.hearit.application.data.player.model.PlayerInfoDataModel
import com.qubacy.hearit.application.data.player.repository._common.source.local._common.model.PlayerInfoDataStoreModel

interface PlayerInfoDataModelDataStoreModelMapper {
  fun map(playerInfoDataModel: PlayerInfoDataModel): PlayerInfoDataStoreModel
  fun map(playerInfoDataStoreModel: PlayerInfoDataStoreModel): PlayerInfoDataModel
}