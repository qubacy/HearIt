package com.qubacy.hearit.application.ui.visual.controller.compose

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application.ui.visual.controller.activity._common.aspect.CloseableActivity
import com.qubacy.hearit.application.ui.visual.controller.activity._common.util.findActivity
import com.qubacy.hearit.application.ui.visual.controller.compose.navigation.HearItNavHost
import com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.components.error.ErrorWidget
import kotlinx.coroutines.CoroutineScope

@Composable
fun HearItApp() {
  val navController = rememberNavController()
  val context = LocalContext.current

  HearItNavHost(
    navController = navController,
    errorWidget = {
      error: ErrorReference,
      snackbarState: SnackbarHostState,
      coroutine: CoroutineScope,
      onDismissRequested: () -> Unit ->
        ErrorWidget(
          error = error,
          snackbarHostState = snackbarState,
          coroutineScope = coroutine,
          onCriticalDismissRequest  = {
            val activity = context.findActivity()

            if (activity is CloseableActivity) activity.close()
          },
          onNotCriticalDismissRequest = onDismissRequested
        )
    }
  )
}

