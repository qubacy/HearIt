package com.qubacy.hearit.application.ui.visual.controller.compose

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application.ui.visual.controller.compose.navigation.HearItNavHost
import com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.components.ErrorWidget
import kotlinx.coroutines.CoroutineScope

@Composable
fun HearItApp() {
  val navController = rememberNavController()

  HearItNavHost(
    navController = navController,
    errorWidget = {
      error: ErrorReference,
      snackbarState: SnackbarHostState,
      coroutine: CoroutineScope,
      context: Context ->
        ErrorWidget(
          error = error,
          snackbarHostState = snackbarState,
          context = context,
          coroutineScope = coroutine
        )
    }
  )
}

