package com.qubacy.hearit.application.ui.visual.controller.compose.screen.home

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.height
import androidx.test.platform.app.InstrumentationRegistry
import com.qubacy.hearit.R
import com.qubacy.hearit.application.ui.visual.resource.theme.HearItTheme
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RadioPlayerTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  private lateinit var _context: Context

  @Before
  fun setup() {
    _context = InstrumentationRegistry.getInstrumentation().targetContext
  }

  @Test
  fun allElementsPresentedInDefaultModeTest() {
    val title = "test title"

    composeTestRule.setContent {
      HearItTheme {
        RadioPlayer(
          title,
          null,
          false,
          false,
          painterResource(id = R.drawable.radio_cover_placeholder),
          {},
          {},
          {},
          modifier = Modifier.fillMaxWidth()
        )
      }
    }

    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_player_cover_image_description)
    )).assertIsDisplayed()
    composeTestRule.onNode(hasText(title)).assertIsDisplayed()
    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_player_prev_button_description)
    )).assertIsDisplayed()
    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_player_play_button_description)
    )).assertIsDisplayed()
    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_player_next_button_description)
    )).assertIsDisplayed()
  }

  @Test
  fun allElementsPresentedInExpandedModeTest() {
    val title = "test title"
    val description = "test description"

    composeTestRule.setContent {
      HearItTheme {
        RadioPlayer(
          title,
          description,
          false,
          true,
          painterResource(id = R.drawable.radio_cover_placeholder),
          {},
          {},
          {},
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_player_cover_image_description)
    )).assertIsDisplayed()
    composeTestRule.onNode(hasText(title)).assertIsDisplayed()
    composeTestRule.onNode(hasText(description)).assertIsDisplayed()
    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_player_prev_button_description)
    )).assertIsDisplayed()
    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_player_play_button_description)
    )).assertIsDisplayed()
    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_player_next_button_description)
    )).assertIsDisplayed()
  }

  @Test
  fun enabledStateChangesElementsTest() {
    val isEnabled = false

    composeTestRule.setContent {
      HearItTheme {
        RadioPlayer(
          "",
          null,
          false,
          false,
          painterResource(id = R.drawable.radio_cover_placeholder),
          {},
          {},
          {},
          enabled = isEnabled
        )
      }
    }

    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_player_prev_button_description))
    ).assertIsNotEnabled()
    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_player_play_button_description))
    ).assertIsNotEnabled()
    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_player_next_button_description))
    ).assertIsNotEnabled()
  }

  @Test
  fun onPrevButtonClickedCalledOnPrevButtonClickedTest() {
    var callFlag = false

    composeTestRule.setContent {
      HearItTheme {
        RadioPlayer(
          "",
          null,
          false,
          false,
          painterResource(id = R.drawable.radio_cover_placeholder),
          { callFlag = true },
          {},
          {},
          modifier = Modifier.fillMaxWidth()
        )
      }
    }

    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_player_prev_button_description)
    )).performClick()

    Assert.assertTrue(callFlag)
  }

  @Test
  fun onPlayButtonClickedCalledOnPlayButtonClickedTest() {
    var callFlag = false

    composeTestRule.setContent {
      HearItTheme {
        RadioPlayer(
          "",
          null,
          false,
          false,
          painterResource(id = R.drawable.radio_cover_placeholder),
          {},
          { callFlag = true },
          {},
          modifier = Modifier.fillMaxWidth()
        )
      }
    }

    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_player_play_button_description)
    )).performClick()

    Assert.assertTrue(callFlag)
  }

  @Test
  fun onNextButtonClickedCalledOnNextButtonClickedTest() {
    var callFlag = false

    composeTestRule.setContent {
      HearItTheme {
        RadioPlayer(
          "",
          null,
          false,
          false,
          painterResource(id = R.drawable.radio_cover_placeholder),
          {},
          {},
          { callFlag = true },
          modifier = Modifier.fillMaxWidth()
        )
      }
    }

    composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_player_next_button_description)
    )).performClick()

    Assert.assertTrue(callFlag)
  }

  @Deprecated("Doesn't pass for now. There's an issue with a px <-> dp converting process")
  @Test
  fun radioCoverTakesAllAvailableSpaceTest() {
    val title = "test title"

    composeTestRule.setContent {
      HearItTheme {
        RadioPlayer(
          title,
          "test description",
          false,
          true,
          painterResource(id = R.drawable.radio_cover_placeholder),
          {},
          {},
          {},
          modifier = Modifier.fillMaxSize()
        )
      }
    }

    val rootSemantics = composeTestRule.onRoot().fetchSemanticsNode()
    val coverBounds = composeTestRule.onNode(hasContentDescription(
      _context.getString(R.string.radio_player_cover_image_description)
    )).getBoundsInRoot()
    val titleSemantics = composeTestRule.onNode(hasText(title)).fetchSemanticsNode()

    val titleBounds = titleSemantics.boundsInRoot
    val titleTopMargin = _context.resources.getDimension(R.dimen.gap_normal).dp // todo: obliged to do this dirt..;

    val rootSize = rootSemantics.size
    val rootVerticalPadding = coverBounds.top // todo: obliged to do this dirt too..;

    val expectedCoverHeight = rootSize.height.dp - rootVerticalPadding - titleTopMargin - titleBounds.top.dp

    val gottenCoverHeight = coverBounds.height

    Assert.assertEquals(expectedCoverHeight, gottenCoverHeight)
  }
}