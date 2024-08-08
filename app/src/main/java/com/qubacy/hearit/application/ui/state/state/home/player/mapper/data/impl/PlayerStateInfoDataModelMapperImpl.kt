package com.qubacy.hearit.application.ui.state.state.home.player.mapper.data.impl

import com.qubacy.hearit.application.data.player.model.PlayerInfoDataModel
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.state.state.home.player.PlayerState
import com.qubacy.hearit.application.ui.state.state.home.player.mapper.data._common.PlayerStateInfoDataModelMapper
import javax.inject.Inject

class PlayerStateInfoDataModelMapperImpl @Inject constructor() : PlayerStateInfoDataModelMapper {
  override fun map(playerState: PlayerState): PlayerInfoDataModel {
    return PlayerInfoDataModel(
      playerState.currentRadio?.id,
      playerState.isRadioPlaying
    )
  }

  override fun map(
    radioPresentation: RadioPresentation?,
    playerInfoDataModel: PlayerInfoDataModel
  ): PlayerState {
    return PlayerState(
      radioPresentation,
      playerInfoDataModel.isPlaying
    )
  }
}