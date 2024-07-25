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
import com.qubacy.hearit.application._common.resources.util.getUriFromResource
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.state.holder.radio.EditRadioViewModel
import com.qubacy.hearit.application.ui.state.state.EditRadioState
import com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.aspect.ImagePickerScreen
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio._common.RadioScreenContent
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio._common.RadioScreenTopAppBarData
import kotlinx.coroutines.CoroutineScope

@Composable
fun EditRadioScreen(
  onBackClicked: () -> Unit,
  onSaved: (Long) -> Unit,
  errorWidget: @Composable (ErrorReference, SnackbarHostState, CoroutineScope) -> Unit,

  modifier: Modifier = Modifier,
  viewModel: EditRadioViewModel = hiltViewModel()
) {
  val state by viewModel.state.observeAsState()

  val context = LocalContext.current

  EditRadioScreen(
    onBackClicked = onBackClicked,
    onSaveClicked = { radioToSave: RadioPresentation -> viewModel.saveRadio(radioToSave) },
    onPickImageClicked = { onPicked -> ImagePickerScreen.pickImage(context, onPicked) },
    errorWidget = errorWidget,
    onSaved = onSaved,
    modifier = modifier,
    radioToEdit = (state as? EditRadioState.Loaded)?.radio,
    isLoading = state is EditRadioState.Loading,
    savedRadio = (state as? EditRadioState.Success)?.radio,
    error = (state as? EditRadioState.Error)?.error
  )
}

@Composable
fun EditRadioScreen(
  onBackClicked: () -> Unit,
  onSaveClicked: (RadioPresentation) -> Unit,
  onPickImageClicked: ((Uri?) -> Unit) -> Unit,
  errorWidget: @Composable (ErrorReference, SnackbarHostState, CoroutineScope) -> Unit,

  onSaved: (Long) -> Unit,

  modifier: Modifier = Modifier,
  radioToEdit: RadioPresentation? = null,
  isLoading: Boolean = false,
  savedRadio: RadioPresentation? = null,
  error: ErrorReference? = null
) {
  if (savedRadio != null) return onSaved(savedRadio.id) // todo: is it ok?

  val coroutineScope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }

  RadioScreenContent(
    topAppBarData = RadioScreenTopAppBarData(
      stringResource(id = R.string.edit_radio_screen_label),
      isLoading,
      onBackClicked
    ),
    onPickImageClicked = onPickImageClicked,
    onSaveClicked = onSaveClicked,
    onCancelClicked = onBackClicked,
    modifier = modifier,
    radioPresentation = radioToEdit
  )

  error?.let {
    errorWidget(it, snackbarHostState, coroutineScope)
  }
}

@Preview
@Composable
fun EditRadioScreen() {
  val resources = LocalContext.current.resources
  val resourceId = R.drawable.space
  val coverUri = resources.getUriFromResource(resourceId)
  val state = EditRadioState.Loaded(
    RadioPresentation(
      0,
      "test title",
      "test description",
      coverUri
    )
  )

  EditRadioScreen(
    onBackClicked = {  },
    onSaveClicked = {  },
    onPickImageClicked = {  },
    errorWidget = {_, _, _ -> },
    onSaved = {  },
    radioToEdit = state.radio
  )
}