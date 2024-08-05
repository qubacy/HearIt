package com.qubacy.hearit.application.data.player.model.mapper.impl

import com.qubacy.hearit.application.data.player.model.PlayerInfoDataModel
import com.qubacy.hearit.application.data.player.model.mapper._common.PlayerInfoDataModelDataStoreModelMapper
import com.qubacy.hearit.application.data.player.repository._common.source.local._common.model.PlayerInfoDataStoreModel
import javax.inject.Inject

class PlayerInfoDataModelDataStoreModelMapperImpl @Inject constructor(

) : PlayerInfoDataModelDataStoreModelMapper {
  override fun map(playerInfoDataModel: PlayerInfoDataModel): PlayerInfoDataStoreModel {
    return PlayerInfoDataStoreModel(
      playerInfoDataModel.curRadioId ?: PlayerInfoDataStoreModel.UNKNOWN_RADIO_ID,
      playerInfoDataModel.isPlaying
    )
  }

  override fun map(playerInfoDataStoreModel: PlayerInfoDataStoreModel): PlayerInfoDataModel {
    return PlayerInfoDataModel(
      playerInfoDataStoreModel.curRadioId.let {
        if (it == PlayerInfoDataStoreModel.UNKNOWN_RADIO_ID) null else it
      },
      playerInfoDataStoreModel.isPlaying
    )
  }
}