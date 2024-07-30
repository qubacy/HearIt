package com.qubacy.hearit.application.data.radio.model.mapper.impl

import com.qubacy.hearit.application.data.radio.model.RadioDataModel
import com.qubacy.hearit.application.data.radio.repository._common.source.local._common.model.RadioLocalModel
import org.junit.Assert
import org.junit.Test

class RadioLocalModelDataModelMapperImplTest {
  private val _instance = RadioLocalModelDataModelMapperImpl()

  @Test
  fun mapTest() {
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
}