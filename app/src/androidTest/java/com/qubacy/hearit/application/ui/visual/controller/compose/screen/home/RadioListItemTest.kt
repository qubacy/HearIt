package com.qubacy.hearit.application.ui.visual.controller.compose.screen.home

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.qubacy.hearit.R
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.visual.resource.theme.HearItTheme
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RadioListItemTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  private lateinit var _context: Context

  @Before
  fun setup() {
    _context = InstrumentationRegistry.getInstrumentation().targetContext
  }

  @Test
  fun allElementsPresentedTest() {
    val radioPresentation = RadioPresentation(
      0, "test title", "test description", url = "http://url.com"
    )

    composeTestRule.setContent {
      HearItTheme {
        RadioListItem(
          radioPresentation = radioPresentation,
          { }
        )
      }
    }

    composeTestRule.onNode(
      hasContentDescription(_context.getString(
        R.string.home_screen_radio_list_item_cover_content_description)
      )
    ).assertIsDisplayed()
    composeTestRule.onNode(hasText(radioPresentation.title)).assertIsDisplayed()
    composeTestRule.onNode(hasText(radioPresentation.description!!)).assertIsDisplayed()
  }

  @Test
  fun onRadioClickedCalledTest() {
    val radioPresentation = RadioPresentation(0, "test title", url = "http://url.com")

    var callFlag = false

    composeTestRule.setContent {
      HearItTheme {
        RadioListItem(
          radioPresentation = radioPresentation,
          onRadioClicked = { callFlag = true }
        )
      }
    }

    composeTestRule.onNode(hasText(radioPresentation.title)).performClick()

    Assert.assertTrue(callFlag)
  }
}