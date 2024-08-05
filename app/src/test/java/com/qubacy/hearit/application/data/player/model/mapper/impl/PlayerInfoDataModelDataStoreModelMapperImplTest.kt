package com.qubacy.hearit.application.data.player.model.mapper.impl

import com.qubacy.hearit.application.data.player.model.PlayerInfoDataModel
import com.qubacy.hearit.application.data.player.repository._common.source.local._common.model.PlayerInfoDataStoreModel
import org.junit.Assert
import org.junit.Test

class PlayerInfoDataModelDataStoreModelMapperImplTest {
  private val _instance = PlayerInfoDataModelDataStoreModelMapperImpl()

  @Test
  fun mapPlayerInfoDataModelToPlayerInfoDataStoreModelTest() {
    data class TestCase(
      val playerInfoDataModel: PlayerInfoDataModel,
      val expectedPlayerInfoDataStoreModel: PlayerInfoDataStoreModel
    )

    val testCases = listOf(
      TestCase(
        playerInfoDataModel = PlayerInfoDataModel(null, false),
        expectedPlayerInfoDataStoreModel = PlayerInfoDataStoreModel(
          PlayerInfoDataStoreModel.UNKNOWN_RADIO_ID, false
        )
      ),
      TestCase(
        playerInfoDataModel = PlayerInfoDataModel(1L, true),
        expectedPlayerInfoDataStoreModel = PlayerInfoDataStoreModel(1L, true)
      )
    )

    for (testCase in testCases) {
      val gottenPlayerInfoDataStoreModel = _instance.map(testCase.playerInfoDataModel)

      Assert.assertEquals(testCase.expectedPlayerInfoDataStoreModel, gottenPlayerInfoDataStoreModel)
    }
  }

  @Test
  fun mapPlayerInfoDataStoreModelToPlayerInfoDataModeTest() {
    data class TestCase(
      val playerInfoDataStoreModel: PlayerInfoDataStoreModel,
      val expectedPlayerInfoDataModel: PlayerInfoDataModel
    )

    val testCases = listOf(
      TestCase(
        playerInfoDataStoreModel = PlayerInfoDataStoreModel(
          PlayerInfoDataStoreModel.UNKNOWN_RADIO_ID, false
        ),
        expectedPlayerInfoDataModel = PlayerInfoDataModel(null, false)
      ),
      TestCase(
        playerInfoDataStoreModel = PlayerInfoDataStoreModel(1L, true),
        expectedPlayerInfoDataModel = PlayerInfoDataModel(1L, true)
      )
    )

    for (testCase in testCases) {
      val gottenPlayerInfoDataModel = _instance.map(testCase.playerInfoDataStoreModel)

      Assert.assertEquals(testCase.expectedPlayerInfoDataModel, gottenPlayerInfoDataModel)
    }
  }
}