package com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.qubacy.hearit.R
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.visual.resource.theme.HearItTheme
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditRadioScreenTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  private lateinit var _context: Context

  @Before
  fun setup() {
    _context = InstrumentationRegistry.getInstrumentation().targetContext
  }

  @Test
  fun onSaveClickedCalledOnSaveButtonClickedTest() {
    var callFlag = false

    composeTestRule.setContent {
      HearItTheme {
        EditRadioScreen(
          onBackClicked = {  },
          onSaveClicked = { callFlag = true },
          onPickImageClicked = {  },
          onSaved = {  },
          onErrorDismissed = {  },
          errorWidget = {_, _, _, _ -> }
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
        EditRadioScreen(
          onBackClicked = { callFlag = true },
          onSaveClicked = {  },
          onPickImageClicked = {  },
          onSaved = {  },
          onErrorDismissed = {  },
          errorWidget = {_, _, _, _ -> }
        )
      }
    }

    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_screen_content_top_app_bar_back_button_description)
    )).performClick()

    Assert.assertTrue(callFlag)
  }

  @Test
  fun onBackClickedCalledOnCancelButtonClickedTest() {
    var callFlag = false

    composeTestRule.setContent {
      HearItTheme {
        EditRadioScreen(
          onBackClicked = { callFlag = true },
          onSaveClicked = {  },
          onPickImageClicked = {  },
          onSaved = {  },
          onErrorDismissed = {  },
          errorWidget = {_, _, _, _ -> }
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
        EditRadioScreen(
          onBackClicked = {  },
          onSaveClicked = {  },
          onPickImageClicked = { callFlag = true },
          onSaved = {  },
          onErrorDismissed = {  },
          errorWidget = {_, _, _, _ -> }
        )
      }
    }

    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_screen_content_image_input_choose_button_description)
    )).performClick()

    Assert.assertTrue(callFlag)
  }

  @Test
  fun onSavedCalledOnSavedRadioTest() {
    val radio = RadioPresentation(0, "test title", url = "http://url.com")

    val expectedRadioId = radio.id

    var gottenRadioId: Long? = null

    composeTestRule.setContent {
      HearItTheme {
        EditRadioScreen(
          onBackClicked = {  },
          onSaveClicked = {  },
          onPickImageClicked = {  },
          onSaved = { gottenRadioId = it },
          onErrorDismissed = {  },
          errorWidget = {_, _, _, _ -> },
          savedRadioId = expectedRadioId
        )
      }
    }

    Assert.assertEquals(expectedRadioId, gottenRadioId)
  }

  @Test
  fun errorShownOnErrorTest() {
    val expectedError = ErrorReference(0)

    var gottenError: ErrorReference? = null

    composeTestRule.setContent {
      HearItTheme {
        EditRadioScreen(
          onBackClicked = {  },
          onSaveClicked = {  },
          onPickImageClicked = {  },
          onSaved = {  },
          onErrorDismissed = {  },
          errorWidget = { errorReference, _, _, _ -> gottenError = errorReference },
          error = expectedError
        )
      }
    }

    Assert.assertEquals(expectedError, gottenError)
  }

  @Test
  fun onErrorDismissedCalledTest() {
    val clickableText = "test text"
    val errorReference = ErrorReference(0)

    var callFlag = false

    composeTestRule.setContent {
      HearItTheme {
        EditRadioScreen(
          onBackClicked = {  },
          onSaveClicked = {  },
          onPickImageClicked = {  },
          onSaved = {  },
          onErrorDismissed = { callFlag = true },
          errorWidget = { _, _, _, handler -> Text(
            text = clickableText, modifier = Modifier.clickable { handler() }
          )},
          error = errorReference
        )
      }
    }

    composeTestRule.onNode(hasText(clickableText)).performClick()

    Assert.assertTrue(callFlag)
  }
}