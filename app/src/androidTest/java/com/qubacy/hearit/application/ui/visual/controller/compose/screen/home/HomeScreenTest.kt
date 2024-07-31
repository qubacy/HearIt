package com.qubacy.hearit.application.ui.visual.controller.compose.screen.home

import android.content.Context
import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.test.platform.app.InstrumentationRegistry
import com.qubacy.hearit.R
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.visual.controller.compose._common.SemanticsKeys
import com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.components.error.provider.ErrorWidgetProvider
import com.qubacy.hearit.application.ui.visual.resource.theme.HearItTheme
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any

class HomeScreenTest {
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
  fun loadingStateAffectsElementsTest() {
    val isLoading = true
    val radioId = 0L
    val radioPresentation = RadioPresentation(radioId, "test title", url = "")
    val radioPresentationList = listOf(radioPresentation)

    composeTestRule.setContent {
      HearItTheme {
        HomeScreen(
          { null },
          {},
          {},
          {},
          {},
          {},
          {},
          {},
          {},
          {},
          _errorWidgetProviderMock,
          isLoading = isLoading,
          radioList = radioPresentationList,
          currentRadioPresentation = radioPresentation
        )
      }
    }

    composeTestRule.onNode(
      hasContentDescription(_context.getString(R.string.home_screen_loading_indicator_description))
    ).assertIsDisplayed()
    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.home_screen_radio_list_item_description_template).format(radioId)
    )).assertHasNoClickAction()

    val playerNode = composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.home_screen_radio_player_description)
    ))

    playerNode.assertHasNoClickAction()

    val enabledValue = playerNode.fetchSemanticsNode().config[SemanticsKeys.enabledPropertyKey]

    Assert.assertEquals(!isLoading, enabledValue)
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
          {},
          {},
          {},
          {},
          {},
          _errorWidgetProviderMock,
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

  @Deprecated("it needs ErrorWidgetProvider.ErrorWidget to be open but this isn't possible for now;")
  @Test
  fun errorDisplayedTest() {
    val expectedErrorReference = ErrorReference(0)

    lateinit var gottenErrorReference: ErrorReference

    composeTestRule.setContent {
      Mockito.`when`(_errorWidgetProviderMock.ErrorWidget(error = any<ErrorReference>()))
        .thenAnswer {
          gottenErrorReference = it.arguments[0] as ErrorReference

          Unit
        }

      HearItTheme {
        HomeScreen(
          { null },
          {},
          {},
          {},
          {},
          {},
          {},
          {},
          {},
          {},
          _errorWidgetProviderMock,
          error = expectedErrorReference
        )
      }
    }

    Assert.assertEquals(expectedErrorReference, gottenErrorReference)
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
          {},
          {},
          {},
          {},
          {},
          _errorWidgetProviderMock,
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
          {},
          {},
          {},
          {},
          {},
          _errorWidgetProviderMock,
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
          {},
          {},
          {},
          {},
          {},
          _errorWidgetProviderMock,
          radioList = radioList
        )
      }
    }

    composeTestRule.onNode(hasContentDescription(radioListItemDescription))
      .performClick()

    Assert.assertTrue(callFlag)
  }

  @Test
  fun onPlayerClickedCalledOnPlayerClickedTest() {
    val radioPresentation = RadioPresentation(0, "Test radio item", url = "http://url.com")
    var callFlag = false

    composeTestRule.setContent {
      HearItTheme {
        HomeScreen(
          { null },
          {},
          {},
          {},
          {},
          onPlayerClicked = { callFlag = true },
          {},
          {},
          {},
          {},
          _errorWidgetProviderMock,
          isLoading = false,
          currentRadioPresentation = radioPresentation
        )
      }
    }

    composeTestRule.onNode(
      hasContentDescription(_context.getString(R.string.home_screen_radio_player_description))
    ).performClick()

    Assert.assertTrue(callFlag)
  }

  @Test
  fun onPlayerBackgroundClickedCalledOnPlayerClickedTest() {
    val radioPresentation = RadioPresentation(0, "Test radio item", url = "http://url.com")
    var callFlag = false

    composeTestRule.setContent {
      HearItTheme {
        HomeScreen(
          { null },
          {},
          {},
          {},
          {},
          {},
          onPlayerBackgroundClicked = { callFlag = true },
          {},
          {},
          {},
          _errorWidgetProviderMock,
          isLoading = false,
          currentRadioPresentation = radioPresentation,
          isPlayerExpanded = true
        )
      }
    }

    composeTestRule.onNode(
      hasContentDescription(_context.getString(R.string.home_screen_radio_player_background_description))
    ).performClick()

    Assert.assertTrue(callFlag)
  }
}