package com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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

@Composable
fun AddRadioScreen(
  onBackClicked: () -> Unit,
  onCreated: (Long) -> Unit,

  modifier: Modifier = Modifier,
  viewModel: AddRadioViewModel = hiltViewModel()
) {
  val state by viewModel.state.observeAsState()

  AddRadioScreen(
    onBackClicked = onBackClicked,
    onCreateClicked = { viewModel.addRadio(it) },
    onCreated = onCreated,
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

  modifier: Modifier = Modifier,
  isLoading: Boolean = false,
  savedRadio: RadioPresentation? = null,
  error: ErrorReference? = null
) {
  if (savedRadio != null) return onCreated(savedRadio.id) // todo: is it ok?

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
}

@Preview
@Composable
fun AddRadioScreen() {
  AddRadioScreen(
    {},
    {},
    {}
  )
}