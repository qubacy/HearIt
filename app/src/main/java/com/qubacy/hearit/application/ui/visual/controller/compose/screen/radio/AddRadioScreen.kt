package com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio

import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.qubacy.hearit.R
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application.ui.state.holder.radio.AddRadioViewModel
import com.qubacy.hearit.application.ui.state.state.AddRadioState
import com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.aspect.ImagePickerScreen
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio._common.RadioScreenContent
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio._common.RadioScreenTopAppBarData
import com.qubacy.hearit.application.ui.state.holder.radio.wrapper.RadioInputWrapper
import kotlinx.coroutines.CoroutineScope

@Composable
fun AddRadioScreen(
  onBackClicked: () -> Unit,
  onCreated: (Long) -> Unit,
  errorWidget: @Composable (
    ErrorReference,
    SnackbarHostState,
    CoroutineScope,
    onDismissRequested: () -> Unit
  ) -> Unit,

  modifier: Modifier = Modifier,
  viewModel: AddRadioViewModel = hiltViewModel()
) {
  val state by viewModel.state.observeAsState() as State<AddRadioState>

  val context = LocalContext.current

  AddRadioScreen(
    onBackClicked = onBackClicked,
    onCreateClicked = { viewModel.addRadio(it) },
    onCreated = onCreated,
    onPickImageClicked = { onPicked -> ImagePickerScreen.pickImage(context, onPicked) },
    onErrorDismissed = { viewModel.consumeCurrentError() },
    errorWidget = errorWidget,
    modifier = modifier,
    isLoading = state.isLoading,
    savedRadioId = state.addedRadioId,
    error = state.error
  )
}

@Composable
fun AddRadioScreen(
  onBackClicked: () -> Unit,
  onCreateClicked: (RadioInputWrapper) -> Unit,
  onCreated: (Long) -> Unit,
  onPickImageClicked: ((Uri?) -> Unit) -> Unit,
  onErrorDismissed: () -> Unit,
  errorWidget: @Composable (ErrorReference, SnackbarHostState, CoroutineScope, () -> Unit) -> Unit,

  modifier: Modifier = Modifier,
  isLoading: Boolean = false,
  savedRadioId: Long? = null,
  error: ErrorReference? = null
) {
  if (savedRadioId != null) return onCreated(savedRadioId) // todo: is it ok?

  RadioScreenContent(
    topAppBarData = RadioScreenTopAppBarData(
      stringResource(id = R.string.add_radio_screen_label),
      isLoading,
      onBackClicked
    ),
    onPickImageClicked = onPickImageClicked,
    onSaveClicked = onCreateClicked,
    onCancelClicked = onBackClicked,
    onErrorDismissed = onErrorDismissed,
    errorWidget = errorWidget,
    modifier = modifier,
    radioPresentation = null,
    error = error
  )
}

@Preview
@Composable
fun AddRadioScreen() {
  AddRadioScreen(
    {},
    {},
    {},
    {},
    {},
    {_, _, _, _ ->}
  )
}