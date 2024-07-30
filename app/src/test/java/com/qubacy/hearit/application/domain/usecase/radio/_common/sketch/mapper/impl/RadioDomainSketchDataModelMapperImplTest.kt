package com.qubacy.hearit.application.domain.usecase.radio._common.sketch.mapper.impl

import com.qubacy.hearit.application.data.radio.model.RadioDataModel
import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.RadioDomainSketch
import org.junit.Assert
import org.junit.Test

class RadioDomainSketchDataModelMapperImplTest {
  private val _instance = RadioDomainSketchDataModelMapperImpl()

  @Test
  fun mapTest() {
    data class TestCase(
      val radioId: Long,
      val radioDomainSketch: RadioDomainSketch,
      val expectedRadioDataModel: RadioDataModel
    )

    val testCases = listOf(
      TestCase(
        radioId = 0L,
        radioDomainSketch = RadioDomainSketch("", null, null, ""),
        expectedRadioDataModel = RadioDataModel(0L, "", null, null, "")
      )
    )

    for (testCase in testCases) {
      val gottenRadioDataModel = _instance.map(testCase.radioId, testCase.radioDomainSketch)

      Assert.assertEquals(testCase.expectedRadioDataModel, gottenRadioDataModel)
    }
  }
}