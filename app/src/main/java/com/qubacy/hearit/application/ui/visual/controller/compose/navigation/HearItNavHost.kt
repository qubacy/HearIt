package com.qubacy.hearit.application.ui.visual.controller.compose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.qubacy.hearit.application.ui.visual.controller._common.navigation.util.consumeResult
import com.qubacy.hearit.application.ui.visual.controller._common.navigation.util.setResult
import com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.components.error.provider.ErrorWidgetProvider
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.home.HomeScreen
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio.AddRadioScreen
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio.EditRadioScreen
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio._common.RadioScreenConst

@Composable
fun HearItNavHost(
  navController: NavHostController,
  errorWidgetProvider: ErrorWidgetProvider
) {
  NavHost(navController = navController, startDestination = Screen.Home.route) {
    composable(route = Screen.Home.route) {
      HomeScreen(
        retrieveSavedRadioId = {
          navController.consumeResult<Long>(RadioScreenConst.SAVED_RADIO_ID_RESULT_KEY)
        },
        onRadioLongPressed = { id -> navController.navigate(Screen.EditRadio.createRoute(id)) },
        onAddRadioClicked = { navController.navigate(Screen.AddRadio.route) },
        errorWidgetProvider = errorWidgetProvider
      )
    }
    composable(route = Screen.AddRadio.route) {
      AddRadioScreen(
        onBackClicked = { navController.navigateUp() },
        onCreated = {
          navController.setResult(RadioScreenConst.SAVED_RADIO_ID_RESULT_KEY, it)
          navController.navigateUp()
        },
        errorWidgetProvider = errorWidgetProvider
      )
    }
    composable(route = Screen.EditRadio.route, arguments = Screen.EditRadio.navArgs) {
      EditRadioScreen(
        onBackClicked = { navController.navigateUp() },
        onSaved = {
          navController.setResult(RadioScreenConst.SAVED_RADIO_ID_RESULT_KEY, it)
          navController.navigateUp()
        },
        errorWidgetProvider = errorWidgetProvider
      )
    }
  }
}