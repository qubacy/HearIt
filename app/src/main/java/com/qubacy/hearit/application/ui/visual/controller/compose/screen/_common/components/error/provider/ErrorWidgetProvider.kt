package com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.components.error.provider

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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

class ErrorWidgetProvider(
    private val defaultOnCriticalErrorDismissRequested: () -> Unit = {}
) {
    @Composable
    fun ErrorWidget(
        error: ErrorReference,
        snackbarModifier: Modifier = Modifier,
        coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
        snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
        showSnackbarManually: Boolean = true,
        onCriticalDismissRequest: () -> Unit = defaultOnCriticalErrorDismissRequested,
        onNotCriticalDismissRequest: () -> Unit = {}
    ) {
        val context = LocalContext.current

        val resolvedError = HearItErrorEnum.fromReference(error).error
        val errorPresentation = ErrorPresentation.fromHearItError(resolvedError, context)

        ErrorWidget(
            errorPresentation,
            snackbarModifier,
            coroutineScope,
            snackbarHostState,
            showSnackbarManually,
            onCriticalDismissRequest,
            onNotCriticalDismissRequest
        )
    }

    @Composable
    fun ErrorWidget(
        error: ErrorPresentation,
        snackbarModifier: Modifier = Modifier,
        coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
        snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
        showSnackbarManually: Boolean = true,
        onCriticalDismissRequest: () -> Unit = defaultOnCriticalErrorDismissRequested,
        onNotCriticalDismissRequest: () -> Unit = { }
    ) {
        if (error.isCritical) {
            AlertDialog(
                title = { Text(text = stringResource(id = R.string.error_dialog_title)) },
                text = { Text(text = error.message) },
                onDismissRequest = onCriticalDismissRequest,
                confirmButton = {
                    TextButton(onClick = onCriticalDismissRequest) {
                        Text(
                            text = stringResource(id = R.string.error_dialog_confirmation_button_text)
                        )
                    }
                }
            )

        } else {
            if (showSnackbarManually) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = snackbarModifier
                )
            }

            LaunchedEffect(key1 = error) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = error.message,
                        withDismissAction = true,
                        duration = SnackbarDuration.Long
                    )

                    onNotCriticalDismissRequest()
                }
            }
        }
    }
}

@Preview
@Composable
fun ErrorWidget() {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val error = ErrorPresentation(0, "test error", false)

    val errorWidgetProvider = ErrorWidgetProvider()

    errorWidgetProvider.ErrorWidget(
        error = error,
        coroutineScope = coroutineScope,
        snackbarHostState = snackbarHostState,
        showSnackbarManually = true
    )
}