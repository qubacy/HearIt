package com.qubacy.hearit.application.domain._common.model.mapper.impl

import com.qubacy.hearit.application.data.radio.model.RadioDataModel
import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.domain._common.model.mapper.data.impl.RadioDomainModelDataModelMapperImpl
import org.junit.Assert
import org.junit.Test
import kotlin.reflect.KClass

class RadioDomainModelDataModelMapperImplTest {
  private val _instance: RadioDomainModelDataModelMapperImpl = RadioDomainModelDataModelMapperImpl()

  @Test
  fun mapTest() {
    data class TestCase(
      val radioDataModel: RadioDataModel,
      val expectedRadioDomainModel: RadioDomainModel? = null,
      val expectedThrowableClass: KClass<*>? = null
    )

    val testCases = listOf(
      TestCase(
        radioDataModel = RadioDataModel(null, "", null, null, ""),
        expectedThrowableClass = AssertionError::class
      ),
      TestCase(
        radioDataModel = RadioDataModel(0L, "", null, null, ""),
        expectedRadioDomainModel = RadioDomainModel(0L, "", null, null, "")
      )
    )

    for (testCase in testCases) {
      if (testCase.expectedThrowableClass != null) {
        try {
          _instance.map(testCase.radioDataModel)

          throw IllegalStateException()

        } catch (e: Throwable) {
          Assert.assertEquals(testCase.expectedThrowableClass, e::class)
        }

      } else {
        val gottenRadioDomainModel = _instance.map(testCase.radioDataModel)

        Assert.assertEquals(testCase.expectedRadioDomainModel, gottenRadioDomainModel)
      }
    }
  }
}