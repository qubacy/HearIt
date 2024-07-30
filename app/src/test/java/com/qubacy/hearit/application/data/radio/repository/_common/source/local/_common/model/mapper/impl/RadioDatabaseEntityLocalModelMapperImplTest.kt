package com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.mapper.impl

import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.dao.entity.RadioDatabaseEntity
import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.RadioLocalModel
import org.junit.Assert
import org.junit.Test

class RadioDatabaseEntityLocalModelMapperImplTest {
  private val _instance = RadioDatabaseEntityLocalModelMapperImpl()

  @Test
  fun mapRadioDatabaseEntityToRadioLocalModelTest() {
    data class TestCase(
      val radioDatabaseEntity: RadioDatabaseEntity,
      val expectedRadioLocalModel: RadioLocalModel
    )

    val testCases = listOf(
      TestCase(
        radioDatabaseEntity = RadioDatabaseEntity(null, "", null, null, ""),
        expectedRadioLocalModel = RadioLocalModel(null, "", null, null, "")
      )
    )

    for (testCase in testCases) {
      val gottenRadioLocalModel = _instance.map(testCase.radioDatabaseEntity)

      Assert.assertEquals(testCase.expectedRadioLocalModel, gottenRadioLocalModel)
    }
  }

  @Test
  fun mapRadioLocalModelToRadioDatabaseEntityTest() {
    data class TestCase(
      val radioLocalModel: RadioLocalModel,
      val expectedRadioDatabaseEntity: RadioDatabaseEntity
    )

    val testCases = listOf(
      TestCase(
        radioLocalModel = RadioLocalModel(null, "", null, null, ""),
        expectedRadioDatabaseEntity = RadioDatabaseEntity(null, "", null, null, "")
      )
    )

    for (testCase in testCases) {
      val gottenRadioDatabaseEntity = _instance.map(testCase.radioLocalModel)

      Assert.assertEquals(testCase.expectedRadioDatabaseEntity, gottenRadioDatabaseEntity)
    }
  }
}