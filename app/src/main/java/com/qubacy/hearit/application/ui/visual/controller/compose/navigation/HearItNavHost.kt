package com.qubacy.hearit.application.ui.visual.controller.compose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.qubacy.hearit.application.ui.visual.controller._common.navigation.util.consumeResult
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.home.HomeScreen
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio.ADDED_RADIO_ID_RESULT_KEY
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio.EditRadioScreen

@Composable
fun HearItNavHost(navController: NavHostController) {
  NavHost(navController = navController, startDestination = Screen.Home.route) {
    composable(route = Screen.Home.route) {
      HomeScreen(
        { navController.consumeResult<Long>(ADDED_RADIO_ID_RESULT_KEY) },
        { id -> navController.navigate(Screen.EditRadio.createRoute(id)) },
        { navController.navigate(Screen.AddRadio.route) }
      )
    }
    composable(route = Screen.AddRadio.route) {
      // todo: implement..
    }
    composable(route = Screen.EditRadio.route, arguments = Screen.EditRadio.navArgs) {
      EditRadioScreen(
        onBackClicked = { navController.navigateUp() },
        onSaveClicked = { navController.navigateUp() } // todo: think of passing a createdRadioId arg!
      )
    }
  }
}