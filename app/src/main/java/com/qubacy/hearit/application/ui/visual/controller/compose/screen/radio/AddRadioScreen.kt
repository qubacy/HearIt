package com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio

import android.content.Context
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
import kotlinx.coroutines.CoroutineScope

@Composable
fun AddRadioScreen(
  onBackClicked: () -> Unit,
  onCreated: (Long) -> Unit,
  errorWidget: @Composable (ErrorReference, SnackbarHostState, CoroutineScope, Context) -> Unit,

  modifier: Modifier = Modifier,
  viewModel: AddRadioViewModel = hiltViewModel()
) {
  val state by viewModel.state.observeAsState()

  AddRadioScreen(
    onBackClicked = onBackClicked,
    onCreateClicked = { viewModel.addRadio(it) },
    onCreated = onCreated,
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
  onCreateClicked: (RadioPresentation) -> Unit,
  onCreated: (Long) -> Unit,
  errorWidget: @Composable (ErrorReference, SnackbarHostState, CoroutineScope, Context) -> Unit,

  modifier: Modifier = Modifier,
  isLoading: Boolean = false,
  savedRadio: RadioPresentation? = null,
  error: ErrorReference? = null
) {
  if (savedRadio != null) return onCreated(savedRadio.id) // todo: is it ok?

  val coroutineScope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }

  val context = LocalContext.current

  RadioScreenContent(
    topAppBarData = RadioScreenTopAppBarData(
      stringResource(id = R.string.edit_radio_screen_label),
      isLoading,
      onBackClicked
    ),
    onPickImageClicked = { onPicked -> ImagePickerScreen.pickImage(context, onPicked) },
    onSaveClicked = onCreateClicked,
    onCancelClicked = onBackClicked,
    modifier = modifier,
    radioPresentation = null
  )

  error?.let {
    errorWidget(it, snackbarHostState, coroutineScope, context)
  }
}

@Preview
@Composable
fun AddRadioScreen() {
  AddRadioScreen(
    {},
    {},
    {},
    {_, _, _, _ ->}
  )
}