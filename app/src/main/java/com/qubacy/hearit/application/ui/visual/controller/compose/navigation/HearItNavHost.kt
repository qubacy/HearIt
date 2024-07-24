package com.qubacy.hearit.application.ui.visual.controller.compose.navigation

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application.ui.visual.controller._common.navigation.util.consumeResult
import com.qubacy.hearit.application.ui.visual.controller._common.navigation.util.setResult
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.home.HomeScreen
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio.AddRadioScreen
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio.EditRadioScreen
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio._common.RadioScreenConst
import kotlinx.coroutines.CoroutineScope

@Composable
fun HearItNavHost(
  navController: NavHostController,
  errorWidget: @Composable (ErrorReference, SnackbarHostState, CoroutineScope, Context) -> Unit
) {
  NavHost(navController = navController, startDestination = Screen.Home.route) {
    composable(route = Screen.Home.route) {
      HomeScreen(
        retrieveSavedRadioId = {
          navController.consumeResult<Long>(RadioScreenConst.SAVED_RADIO_ID_RESULT_KEY)
        },
        onRadioClicked = { id -> navController.navigate(Screen.EditRadio.createRoute(id)) },
        onAddRadioClicked = { navController.navigate(Screen.AddRadio.route) },
        errorWidget = errorWidget
      )
    }
    composable(route = Screen.AddRadio.route) {
      AddRadioScreen(
        onBackClicked = { navController.navigateUp() },
        onCreated = {
          navController.setResult(RadioScreenConst.SAVED_RADIO_ID_RESULT_KEY, it)
          navController.navigateUp()
        },
        errorWidget = errorWidget
      )
    }
    composable(route = Screen.EditRadio.route, arguments = Screen.EditRadio.navArgs) {
      EditRadioScreen(
        onBackClicked = { navController.navigateUp() },
        onSaved = {
          navController.setResult(RadioScreenConst.SAVED_RADIO_ID_RESULT_KEY, it)
          navController.navigateUp()
        },
        errorWidget = errorWidget
      )
    }
  }
}