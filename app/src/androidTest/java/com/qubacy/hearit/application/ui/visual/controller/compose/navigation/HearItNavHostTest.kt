package com.qubacy.hearit.application.ui.visual.controller.compose.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HearItNavHostTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  private lateinit var _testNavHostController: TestNavHostController

  @Before
  fun setup() {
    composeTestRule.setContent {
      _testNavHostController = TestNavHostController(LocalContext.current)

      _testNavHostController.navigatorProvider.addNavigator(ComposeNavigator())

      HearItNavHost(navController = _testNavHostController) { _, _, _ -> }
    }
  }

  @Deprecated("Doesn't work 'cause of the Hilt (hiltViewModel() calls, to be precise);")
  @Test
  fun homeStartDestinationTest() {
    val expectedRoute = Screen.Home.route

    val gottenRoute = _testNavHostController.currentBackStackEntry?.destination?.route

    Assert.assertEquals(expectedRoute, gottenRoute)
  }
}