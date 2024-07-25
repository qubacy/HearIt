package com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio

import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
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
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.state.holder.radio.AddRadioViewModel
import com.qubacy.hearit.application.ui.state.state.AddRadioState
import com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.aspect.ImagePickerScreen
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio._common.RadioScreenContent
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio._common.RadioScreenTopAppBarData
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio._common.wrapper.RadioInputWrapper
import kotlinx.coroutines.CoroutineScope

@Composable
fun AddRadioScreen(
  onBackClicked: () -> Unit,
  onCreated: (Long) -> Unit,
  errorWidget: @Composable (ErrorReference, SnackbarHostState, CoroutineScope) -> Unit,

  modifier: Modifier = Modifier,
  viewModel: AddRadioViewModel = hiltViewModel()
) {
  val state by viewModel.state.observeAsState()

  val context = LocalContext.current

  AddRadioScreen(
    onBackClicked = onBackClicked,
    onCreateClicked = { viewModel.addRadio(it) },
    onCreated = onCreated,
    onPickImageClicked = { onPicked -> ImagePickerScreen.pickImage(context, onPicked) },
    errorWidget = errorWidget,
    modifier = modifier,
    isLoading = state is AddRadioState.Loading,
    savedRadio = (state as? AddRadioState.Success)?.radio,
    error = (state as? AddRadioState.Error)?.error
  )
}

@Composable
fun AddRadioScreen(
  onBackClicked: () -> Unit,
  onCreateClicked: (RadioInputWrapper) -> Unit,
  onCreated: (Long) -> Unit,
  onPickImageClicked: ((Uri?) -> Unit) -> Unit,
  errorWidget: @Composable (ErrorReference, SnackbarHostState, CoroutineScope) -> Unit,

  modifier: Modifier = Modifier,
  isLoading: Boolean = false,
  savedRadio: RadioPresentation? = null,
  error: ErrorReference? = null
) {
  if (savedRadio != null) return onCreated(savedRadio.id) // todo: is it ok?

  val coroutineScope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }

  RadioScreenContent(
    topAppBarData = RadioScreenTopAppBarData(
      stringResource(id = R.string.add_radio_screen_label),
      isLoading,
      onBackClicked
    ),
    onPickImageClicked = onPickImageClicked,
    onSaveClicked = onCreateClicked,
    onCancelClicked = onBackClicked,
    modifier = modifier,
    radioPresentation = null
  )

  error?.let {
    errorWidget(it, snackbarHostState, coroutineScope)
  }
}

@Preview
@Composable
fun AddRadioScreen() {
  AddRadioScreen(
    {},
    {},
    {},
    {},
    {_, _, _ ->}
  )
}