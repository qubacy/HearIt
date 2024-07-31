package com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.components.error.provider.ErrorWidgetProvider

@Composable
fun AddRadioScreen(
  onBackClicked: () -> Unit,
  onCreated: (Long) -> Unit,
  errorWidgetProvider: ErrorWidgetProvider,

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
    errorWidgetProvider = errorWidgetProvider,
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
  errorWidgetProvider: ErrorWidgetProvider,

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
    errorWidgetProvider = errorWidgetProvider,
    modifier = modifier,
    radioPresentation = null,
    error = error
  )
}

@Preview
@Composable
fun AddRadioScreen() {
  val errorWidgetProvider = ErrorWidgetProvider()

  AddRadioScreen(
    {},
    {},
    {},
    {},
    {},
    errorWidgetProvider = errorWidgetProvider
  )
}