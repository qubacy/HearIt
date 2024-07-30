package com.qubacy.hearit.application.data.radio.model.mapper.impl

import com.qubacy.hearit.application.data.radio.model.RadioDataModel
import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.RadioLocalModel
import org.junit.Assert
import org.junit.Test

class RadioDataModelLocalModelMapperImplTest {
  private val _instance = RadioDataModelLocalModelMapperImpl()

  @Test
  fun mapLocalModelToDataModelTest() {
    data class TestCase(
      val radioLocalModel: RadioLocalModel,
      val expectedRadioDataModel: RadioDataModel
    )

    val testCases = listOf(
      TestCase(
        radioLocalModel = RadioLocalModel(0, "", null, null, ""),
        expectedRadioDataModel = RadioDataModel(0, "", null, null, "")
      ),
    )

    for (testCase in testCases) {
      val gottenRadioDataModel = _instance.map(testCase.radioLocalModel)

      Assert.assertEquals(testCase.expectedRadioDataModel, gottenRadioDataModel)
    }
  }

  @Test
  fun mapDataModelToLocalModelTest() {
    data class TestCase(
      val radioDataModel: RadioDataModel,
      val expectedRadioLocalModel: RadioLocalModel
    )

    val testCases = listOf(
      TestCase(
        radioDataModel = RadioDataModel(0, "", null, null, ""),
        expectedRadioLocalModel = RadioLocalModel(0, "", null, null, "")
      ),
    )

    for (testCase in testCases) {
      val gottenRadioLocalModel = _instance.map(testCase.radioDataModel)

      Assert.assertEquals(testCase.expectedRadioLocalModel, gottenRadioLocalModel)
    }
  }
}