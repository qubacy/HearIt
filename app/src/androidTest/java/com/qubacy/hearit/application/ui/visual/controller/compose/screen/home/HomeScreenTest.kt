package com.qubacy.hearit.application.ui.visual.controller.compose.screen.home

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.test.platform.app.InstrumentationRegistry
import com.qubacy.hearit.R
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.visual.resource.theme.HearItTheme
import kotlinx.coroutines.test.runTest
import org.junit.Assert
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

  @Test
  fun loadingIndicatorVisibleOnLoadingStateTest() {
    composeTestRule.setContent {
      HearItTheme {
        HomeScreen(
          { null },
          {},
          {},
          {},
          {},
          { _, _, _, _ -> },
          isLoading = true
        )
      }
    }

    composeTestRule.onNode(
      hasContentDescription(_context.getString(R.string.home_screen_loading_indicator_description))
    ).assertIsDisplayed()
  }

  @Test
  fun radioListItemsDisplayedTest() {
    val radioPresentation = RadioPresentation(0, "Test radio", url = "http://url.com")
    val radioList = listOf(radioPresentation)

    composeTestRule.setContent {
      HearItTheme {
        HomeScreen(
          { null },
          {},
          {},
          {},
          {},
          { _, _, _, _ -> },
          radioList = radioList
        )
      }
    }

    val radioListItemDescriptionTemplate = _context
      .getString(R.string.home_screen_radio_list_item_description_template)

    for (radioListItem in radioList) {
      composeTestRule.onNode(
        hasContentDescription(radioListItemDescriptionTemplate.format(radioListItem.id))
      ).assertIsDisplayed()
    }
  }

  @Test
  fun errorDisplayedTest() {
    val error = ErrorReference(0)
    val expectedErrorText = "Test error ${error.id}"

    composeTestRule.setContent {
      HearItTheme {
        HomeScreen(
          { null },
          {},
          {},
          {},
          {},
          { _, _, _, _ -> Text(text = expectedErrorText)},
          error = error
        )
      }
    }

    composeTestRule.onNode(hasText(expectedErrorText)).assertIsDisplayed()
  }

  @Test
  fun onAddRadioClickedCalledTest() = runTest {
    var callFlag = false

    composeTestRule.setContent {
      HearItTheme {
        HomeScreen(
          { null },
          {},
          {},
          onAddRadioClicked = { callFlag = true },
          {},
          { _, _, _, _ -> },
          isLoading = false
        )
      }
    }

    composeTestRule.onNode(
      hasContentDescription(_context.getString(R.string.home_screen_fab_description))
    ).performClick()

    Assert.assertTrue(callFlag)
  }

  @Test
  fun onRadioLongPressedCalledTest() {
    val radioPresentation = RadioPresentation(0, "Test radio item", url = "http://url.com")
    val radioList = listOf(radioPresentation)

    val radioListItemDescription = _context
      .getString(R.string.home_screen_radio_list_item_description_template)
      .format(radioPresentation.id)

    var callFlag = false

    composeTestRule.setContent {
      HearItTheme {
        HomeScreen(
          { null },
          {},
          onRadioLongPressed = { callFlag = true },
          {},
          {},
          { _, _, _, _ -> },
          radioList = radioList
        )
      }
    }

    composeTestRule.onNode(hasContentDescription(radioListItemDescription))
      .performTouchInput { longClick() }

    Assert.assertTrue(callFlag)
  }

  @Test
  fun onRadioPickedCalledTest() {
    val radioPresentation = RadioPresentation(0, "Test radio item", url = "http://url.com")
    val radioList = listOf(radioPresentation)

    val radioListItemDescription = _context
      .getString(R.string.home_screen_radio_list_item_description_template)
      .format(radioPresentation.id)

    var callFlag = false

    composeTestRule.setContent {
      HearItTheme {
        HomeScreen(
          { null },
          onRadioPicked = { callFlag = true },
          {},
          {},
          {},
          { _, _, _, _ -> },
          radioList = radioList
        )
      }
    }

    composeTestRule.onNode(hasContentDescription(radioListItemDescription))
      .performClick()

    Assert.assertTrue(callFlag)
  }

  @Test
  fun onErrorDismissedCalledTest() {
    val clickableText = "test text"
    val errorReference = ErrorReference(0)

    var callFlag = false

    composeTestRule.setContent {
      HearItTheme {
        HomeScreen(
          { null },
          {},
          {},
          {},
          onErrorDismissed = { callFlag = true },
          { _, _, _, handler -> Text(
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