package com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio._common

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.qubacy.hearit.R
import com.qubacy.hearit.application._common.resources.util.getUriFromResource
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio._common.wrapper.RadioInputWrapper
import com.qubacy.hearit.application.ui.visual.resource.theme.HearItTheme
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RadioScreenContentTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  private lateinit var _context: Context

  @Before
  fun setup() {
    _context = InstrumentationRegistry.getInstrumentation().targetContext
  }

  @Test
  fun allElementsPresentedTest() {
    val topAppBarData = RadioScreenTopAppBarData("test app bar title", false, {  })

    val imageUri = _context.resources.getUriFromResource(R.drawable.ic_launcher_background)
    val radioPresentation = RadioPresentation(
      0, "test title", "test description", imageUri, url = "http://url.com"
    )

    composeTestRule.setContent {
      HearItTheme {
        RadioScreenContent(
          onPickImageClicked = {  },
          onSaveClicked = {  },
          onCancelClicked = {  },
          radioPresentation = radioPresentation,
          topAppBarData = topAppBarData
        )
      }
    }

    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_screen_content_top_app_bar_description))
    ).assertIsDisplayed()
    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_screen_content_title_input_description))
    ).assertIsDisplayed()
    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_screen_content_description_input_description))
    ).assertIsDisplayed()
    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_screen_content_url_input_description)
    )).assertIsDisplayed()
    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_screen_content_image_input_icon_description))
    ).assertIsDisplayed()
    composeTestRule.onNode(hasText(
      _context.getString(R.string.radio_screen_content_image_input_hint)
    )).assertIsDisplayed()
    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_screen_content_image_input_choose_button_description))
    ).assertIsDisplayed()
    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_screen_content_image_input_preview_image_description))
    ).assertIsDisplayed()
    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_screen_content_image_input_preview_close_button_description))
    ).assertIsDisplayed()
  }

  @Test
  fun imagePreviewClosedOnCloseButtonClickedTest() {
    val imageUri = _context.resources.getUriFromResource(R.drawable.ic_launcher_background)
    val radioPresentation = RadioPresentation(
      0, "test title", "test description", imageUri, url = "http://url.com"
    )

    composeTestRule.setContent {
      HearItTheme {
        RadioScreenContent(
          onPickImageClicked = {  },
          onSaveClicked = {  },
          onCancelClicked = {  },
          radioPresentation = radioPresentation
        )
      }
    }

    composeTestRule.onNode(
      hasContentDescription(
        _context.getString(R.string.radio_screen_content_image_input_preview_close_button_description)
      )
    ).performClick()

    composeTestRule.onNode(
      hasContentDescription(
        _context.getString(R.string.radio_screen_content_image_input_preview_image_description)
      )
    ).assertDoesNotExist()
    composeTestRule.onNode(
      hasContentDescription(
        _context.getString(R.string.radio_screen_content_image_input_preview_close_button_description)
      )
    ).assertDoesNotExist()
  }

  @Test
  fun onPickImageClickedCalledOnChooseImageButtonClickedTest() {
    var callFlag = false

    composeTestRule.setContent {
      HearItTheme {
        RadioScreenContent(
          onPickImageClicked = { callFlag = true },
          onSaveClicked = {  },
          onCancelClicked = {  }
        )
      }
    }

    composeTestRule.onNode(
      hasContentDescription(
        _context.getString(R.string.radio_screen_content_image_input_choose_button_description)
      )
    ).performClick()

    Assert.assertTrue(callFlag)
  }

  @Test
  fun onSaveClickedCalledOnSaveButtonClickedTest() {
    val radioPresentation = RadioPresentation(title = "test title", url = "http://url.com")
    val expectedRadioInputWrapper = RadioInputWrapper(
      radioPresentation.title, url = radioPresentation.url
    )

    var gottenRadioInputWrapper: RadioInputWrapper? = null

    composeTestRule.setContent {
      HearItTheme {
        RadioScreenContent(
          onPickImageClicked = {  },
          onSaveClicked = { gottenRadioInputWrapper = it },
          onCancelClicked = {  },
          radioPresentation = radioPresentation
        )
      }
    }

    composeTestRule.onNode(
      hasText(_context.getString(R.string.radio_screen_content_save_button_text))
    ).performClick()

    Assert.assertEquals(expectedRadioInputWrapper, gottenRadioInputWrapper)
  }

  @Test
  fun onCancelClickedCalledOnCancelButtonClickedTest() {
    var callFlag = false

    composeTestRule.setContent {
      HearItTheme {
        RadioScreenContent(
          onPickImageClicked = {  },
          onSaveClicked = {  },
          onCancelClicked = { callFlag = true }
        )
      }
    }

    composeTestRule.onNode(
      hasText(_context.getString(R.string.radio_screen_content_cancel_button_text))
    ).performClick()

    Assert.assertTrue(callFlag)
  }

  @Test
  fun onCancelClickedCalledOnBackButtonClickedTest() {
    var callFlag = false

    composeTestRule.setContent {
      HearItTheme {
        RadioScreenContent(
          onPickImageClicked = {  },
          onSaveClicked = {  },
          onCancelClicked = {  },
          topAppBarData = RadioScreenTopAppBarData("", false, { callFlag = true })
        )
      }
    }

    composeTestRule.onNode(
      hasContentDescription(
        _context.getString(R.string.radio_screen_content_top_app_bar_back_button_description)
      )
    ).performClick()

    Assert.assertTrue(callFlag)
  }
}