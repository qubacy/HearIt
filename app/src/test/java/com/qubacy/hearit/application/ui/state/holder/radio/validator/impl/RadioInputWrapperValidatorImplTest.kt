package com.qubacy.hearit.application.ui.state.holder.radio.validator.impl

import com.qubacy.hearit.application.ui.state.holder.radio.wrapper.RadioInputWrapper
import org.junit.Assert
import org.junit.Test

class RadioInputWrapperValidatorImplTest {
  @Test
  fun validateTest() {
    data class TestCase(
      val radioInputWrapper: RadioInputWrapper,
      val isUrlValid: Boolean,
      val expectedIsValid: Boolean
    )

    val testCases = listOf(
      TestCase(
        radioInputWrapper = RadioInputWrapper("", url = ""),
        isUrlValid = true,
        expectedIsValid = false
      ),
      TestCase(
        radioInputWrapper = RadioInputWrapper(" ", url = ""),
        isUrlValid = true,
        expectedIsValid = false
      ),
      TestCase(
        radioInputWrapper = RadioInputWrapper("f", url = ""),
        isUrlValid = false,
        expectedIsValid = false
      ),
      TestCase(
        radioInputWrapper = RadioInputWrapper("f", url = ""),
        isUrlValid = true,
        expectedIsValid = true
      ),
      TestCase(
        radioInputWrapper = RadioInputWrapper("f", description = "", url = ""),
        isUrlValid = true,
        expectedIsValid = false
      ),
      TestCase(
        radioInputWrapper = RadioInputWrapper("f", description = " ", url = ""),
        isUrlValid = true,
        expectedIsValid = false
      ),
      TestCase(
        radioInputWrapper = RadioInputWrapper("f", description = "f", url = ""),
        isUrlValid = true,
        expectedIsValid = true
      ),
    )

    for (testCase in testCases) {
      val validator = RadioInputWrapperValidatorImpl { testCase.isUrlValid }
      val gottenIsValid = validator.validate(testCase.radioInputWrapper)

      Assert.assertEquals(testCase.expectedIsValid, gottenIsValid)
    }
  }
}