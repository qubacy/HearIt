package com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio

import android.content.Context
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.qubacy.hearit.R
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.components.error.provider.ErrorWidgetProvider
import com.qubacy.hearit.application.ui.visual.resource.theme.HearItTheme
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class AddRadioScreenTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  private lateinit var _context: Context

  private lateinit var _errorWidgetProviderMock: ErrorWidgetProvider

  @Before
  fun setup() {
    _context = InstrumentationRegistry.getInstrumentation().targetContext
    _errorWidgetProviderMock = mockErrorWidgetProvider()
  }

  private fun mockErrorWidgetProvider(): ErrorWidgetProvider {
    return Mockito.mock(ErrorWidgetProvider::class.java)
  }

  @Test
  fun onCreatedCalledOnSavedRadioTest() {
    val radio = RadioPresentation(0, "test title", url = "http://url.com")

    val expectedRadioId = radio.id

    var gottenRadioId: Long? = null

    composeTestRule.setContent {
      HearItTheme {
        AddRadioScreen(
          onBackClicked = {  },
          onCreateClicked = {  },
          onCreated = { gottenRadioId = it },
          onPickImageClicked = {  },
          onErrorDismissed = {  },
          errorWidgetProvider = _errorWidgetProviderMock,
          savedRadioId = expectedRadioId
        )
      }
    }

    Assert.assertEquals(expectedRadioId, gottenRadioId)
  }

  @Test
  fun onCreateClickedCalledOnSaveClickedTest() {
    var callFlag = false

    composeTestRule.setContent {
      HearItTheme {
        AddRadioScreen(
          onBackClicked = {  },
          onCreateClicked = { callFlag = true },
          onCreated = {  },
          onPickImageClicked = {  },
          onErrorDismissed = {  },
          errorWidgetProvider = _errorWidgetProviderMock
        )
      }
    }

    composeTestRule.onNode(hasText(
      _context.getString(R.string.radio_screen_content_save_button_text)
    )).performClick()

    Assert.assertTrue(callFlag)
  }

  @Test
  fun onBackClickedCalledOnBackButtonClickedTest() {
    var callFlag = false

    composeTestRule.setContent {
      HearItTheme {
        AddRadioScreen(
          onBackClicked = { callFlag = true },
          onCreateClicked = {  },
          onCreated = {  },
          onPickImageClicked = {  },
          onErrorDismissed = {  },
          errorWidgetProvider = _errorWidgetProviderMock
        )
      }
    }

    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_screen_content_top_app_bar_back_button_description)
    )).performClick()

    Assert.assertTrue(callFlag)
  }

  @Test
  fun onBackClickedCalledOnCancelClickedTest() {
    var callFlag = false

    composeTestRule.setContent {
      HearItTheme {
        AddRadioScreen(
          onBackClicked = { callFlag = true },
          onCreateClicked = {  },
          onCreated = {  },
          onPickImageClicked = {  },
          onErrorDismissed = {  },
          errorWidgetProvider = _errorWidgetProviderMock
        )
      }
    }

    composeTestRule.onNode(hasText(
      _context.getString(R.string.radio_screen_content_cancel_button_text)
    )).performClick()

    Assert.assertTrue(callFlag)
  }

  @Test
  fun onPickImageClickedCalledOnChooseImageButtonClickedTest() {
    var callFlag = false

    composeTestRule.setContent {
      HearItTheme {
        AddRadioScreen(
          onBackClicked = {  },
          onCreateClicked = {  },
          onCreated = {  },
          onPickImageClicked = { callFlag = true },
          onErrorDismissed = {  },
          errorWidgetProvider = _errorWidgetProviderMock
        )
      }
    }

    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_screen_content_image_input_choose_button_description)
    )
    ).performClick()

    Assert.assertTrue(callFlag)
  }
}