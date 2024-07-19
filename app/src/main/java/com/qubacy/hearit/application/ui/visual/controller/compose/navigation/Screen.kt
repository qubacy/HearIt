package com.qubacy.hearit.application.ui.visual.controller.compose.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
  val route: String,
  val navArgs: List<NamedNavArgument> = emptyList()
) {
  data object Home : Screen("home")

  data object AddRadio : Screen("addRadio")

  data object EditRadio : Screen(
    "editRadio/{radioId}",
    listOf(navArgument("radioId") { type = NavType.LongType })
  ) {
    fun createRoute(id: Long): String {
      return "editRadio/$id"
    }
  }
}