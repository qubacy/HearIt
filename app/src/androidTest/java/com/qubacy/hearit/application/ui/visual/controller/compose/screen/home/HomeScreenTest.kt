package com.qubacy.hearit.application.ui.visual.controller.compose.screen.home

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.qubacy.hearit.R
import com.qubacy.hearit.application.ui.visual.resource.theme.HearItTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  private lateinit var _context: Context

  @Before
  fun setup() {
    _context = InstrumentationRegistry.getInstrumentation().targetContext
  }

  @Test
  fun allElementsPresentedTest() {
    composeTestRule.setContent {
      HearItTheme {
        HomeScreen()
      }
    }

    composeTestRule.onNode(
      hasContentDescription(_context.getString(R.string.home_screen_top_app_bar_description))
    ).assertIsDisplayed()
    composeTestRule.onNode(
      hasContentDescription(_context.getString(R.string.home_screen_radio_list_description))
    ).assertIsDisplayed()
    composeTestRule.onNode(
      hasContentDescription(_context.getString(R.string.home_screen_fab_description))
    ).assertIsDisplayed()
  }
}