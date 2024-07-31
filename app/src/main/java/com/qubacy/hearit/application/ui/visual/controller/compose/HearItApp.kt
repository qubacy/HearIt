package com.qubacy.hearit.application.ui.visual.controller.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.qubacy.hearit.application.ui.visual.controller.activity._common.aspect.CloseableActivity
import com.qubacy.hearit.application.ui.visual.controller.activity._common.util.findActivity
import com.qubacy.hearit.application.ui.visual.controller.compose.navigation.HearItNavHost
import com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.components.error.provider.ErrorWidgetProvider

@Composable
fun HearItApp() {
  val navController = rememberNavController()
  val context = LocalContext.current

  HearItNavHost(
    navController = navController,
    errorWidgetProvider = ErrorWidgetProvider(
      defaultOnCriticalErrorDismissRequested = {
        val activity = context.findActivity()

        if (activity is CloseableActivity) activity.close()
      }
    )
  )
}

