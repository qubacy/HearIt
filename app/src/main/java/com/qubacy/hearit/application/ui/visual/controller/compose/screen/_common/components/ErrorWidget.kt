package com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
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
import com.qubacy.hearit.application.ui.visual.controller.activity._common.aspect.CloseableActivity
import com.qubacy.hearit.application.ui.visual.controller.activity._common.util.findActivity
import com.qubacy.hearit.application.ui.visual.resource.error.HearItError
import com.qubacy.hearit.application.ui.visual.resource.error.HearItErrorEnum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ErrorWidget(
    error: ErrorReference,
    snackbarHostState: SnackbarHostState,
    context: Context,
    coroutineScope: CoroutineScope
) {
    val resolvedError = HearItErrorEnum.fromReference(error).error

    ErrorWidget(resolvedError, snackbarHostState, context, coroutineScope)
}

@Composable
fun ErrorWidget(
    error: HearItError,
    snackbarHostState: SnackbarHostState,
    context: Context,
    coroutineScope: CoroutineScope
) {
    val message = context.getString(error.messageResId)

    if (error.isCritical) {
        val onDismissRequest = {
            Log.d("Error", "dismissed a critical error;")

            val activity = context.findActivity()

            if (activity is CloseableActivity) activity.close()
        }

        AlertDialog(
            title = { Text(text = stringResource(id = R.string.error_dialog_title)) },
            text = { Text(text = message) },
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
                    message = message,
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

    val context = LocalContext.current

    val error = HearItError(0, R.string.app_name, true)
    
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        
    }

    ErrorWidget(
        error,
        snackbarHostState, context, coroutineScope
    )
}