package com.qubacy.hearit.application.ui.visual.controller.compose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.home.HomeScreen

@Composable
fun HearItNavHost(navController: NavHostController) {
  NavHost(navController = navController, startDestination = Screen.Home.route) {
    composable(route = Screen.Home.route) {
      HomeScreen(
        { id -> navController.navigate(Screen.EditRadio.createRoute(id)) },
        { navController.navigate(Screen.AddRadio.route) }
      )
    }
    composable(route = Screen.AddRadio.route) {
      // todo: implement..
    }
    composable(route = Screen.EditRadio.route) {
      // todo: implement..
    }
  }
}