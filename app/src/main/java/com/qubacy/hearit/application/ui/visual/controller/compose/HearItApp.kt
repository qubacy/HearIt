package com.qubacy.hearit.application.ui.visual.controller.compose

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.qubacy.hearit.application.ui.visual.controller.compose.navigation.HearItNavHost

@Composable
fun HearItApp() {
  val navController = rememberNavController()

  HearItNavHost(navController = navController)
}

