package com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.components

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.qubacy.hearit.R
import com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.components.error.presentation.ErrorPresentation
import com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.components.error.ErrorWidget
import com.qubacy.hearit.application.ui.visual.resource.theme.HearItTheme
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ErrorWidgetTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  private lateinit var _context: Context

  @Before
  fun setup() {
    _context = InstrumentationRegistry.getInstrumentation().targetContext
  }

  @Test
  fun allElementsPresentedInCriticalDialogTest() {
    val error = ErrorPresentation(0, "test error", true)

    val titleText = _context.getString(R.string.error_dialog_title)
    val confirmationButtonText = _context.getString(R.string.error_dialog_confirmation_button_text)

    composeTestRule.setContent {
      HearItTheme {
        ErrorWidget(error = error)
      }
    }

    composeTestRule.onNode(isDialog()).assertIsDisplayed()
    composeTestRule.onNode(hasText(titleText)).assertIsDisplayed()
    composeTestRule.onNode(hasText(error.message)).assertIsDisplayed()
    composeTestRule.onNode(hasText(confirmationButtonText)).assertIsDisplayed()
  }

  @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
  @Test
  fun allElementsPresentedInSnackbarTest() {
    val error = ErrorPresentation(0, "test error", false)

    composeTestRule.setContent {
      HearItTheme {
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
          snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) {}

        ErrorWidget(
          error,
          coroutineScope = coroutineScope,
          snackbarHostState = snackbarHostState,
          showSnackbarManually = false
        )
      }
    }

    composeTestRule.onNode(hasText(error.message)).assertIsDisplayed()
  }

  @Test
  fun criticalDialogCallsOnDismissRequestTest() {
    val error = ErrorPresentation(0, "test error", true)

    var callFlag = false

    composeTestRule.setContent {
      HearItTheme {
        ErrorWidget(error = error, onCriticalDismissRequest = { callFlag = true })
      }
    }

    composeTestRule.onNode(
      hasText(_context.getString(R.string.error_dialog_confirmation_button_text))
    ).performClick()

    Assert.assertTrue(callFlag)
  }
}