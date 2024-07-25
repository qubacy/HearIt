package com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.components.error

import android.annotation.SuppressLint
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.qubacy.hearit.R
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.components.error.presentation.ErrorPresentation
import com.qubacy.hearit.application.ui.visual.resource.error.HearItErrorEnum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ErrorWidget(
    error: ErrorReference,
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onDismissRequest: () -> Unit = {}
) {
    val context = LocalContext.current

    val resolvedError = HearItErrorEnum.fromReference(error).error
    val errorPresentation = ErrorPresentation.fromHearItError(resolvedError, context)

    ErrorWidget(errorPresentation, coroutineScope, snackbarHostState, onDismissRequest)
}

@Composable
fun ErrorWidget(
  error: ErrorPresentation,
  coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
  snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
  onDismissRequest: () -> Unit = { }
) {
    if (error.isCritical) {
        AlertDialog(
            title = { Text(text = stringResource(id = R.string.error_dialog_title)) },
            text = { Text(text = error.message) },
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(
                        text = stringResource(id = R.string.error_dialog_confirmation_button_text)
                    )
                }
            }
        )

    } else {
        LaunchedEffect(key1 = error) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = error.message,
                    withDismissAction = true,
                    duration = SnackbarDuration.Long
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun ErrorWidget() {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val error = ErrorPresentation(0, "test error", true)
    
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {}

    ErrorWidget(error, coroutineScope, snackbarHostState)
}