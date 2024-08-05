package com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.qubacy.hearit.R
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application._common.resources.util.getUriFromResource
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.state.holder.radio.EditRadioViewModel
import com.qubacy.hearit.application.ui.state.state.radio.EditRadioState
import com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.aspect.ImagePickerScreen
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio._common.RadioScreenContent
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio._common.RadioScreenTopAppBarData
import com.qubacy.hearit.application.ui.state.holder.radio.wrapper.RadioInputWrapper
import com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.components.error.provider.ErrorWidgetProvider

@Composable
fun EditRadioScreen(
  onBackClicked: () -> Unit,
  onSaved: (Long) -> Unit,
  errorWidgetProvider: ErrorWidgetProvider,

  modifier: Modifier = Modifier,
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
  viewModel: EditRadioViewModel = hiltViewModel()
) {
  val state by viewModel.state.observeAsState() as State<EditRadioState>

  val context = LocalContext.current

  EditRadioScreen(
    onBackClicked = onBackClicked,
    onSaveClicked = { viewModel.saveRadio(it) },
    onPickImageClicked = { onPicked -> ImagePickerScreen.pickImage(context, onPicked) },
    onErrorDismissed = { viewModel.consumeCurrentError() },
    errorWidgetProvider = errorWidgetProvider,
    onSaved = onSaved,
    modifier = modifier,
    radioToEdit = state.loadedRadio,
    isLoading = state.isLoading,
    savedRadioId = state.savedRadioId,
    error = state.error
  )

  SetupRadioObserver(lifecycleOwner, viewModel)
}

@Composable
fun SetupRadioObserver(lifecycleOwner: LifecycleOwner, viewModel: EditRadioViewModel) {
  DisposableEffect(key1 = lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
      if (event == Lifecycle.Event.ON_START) viewModel.observeRadio()
      else if (event == Lifecycle.Event.ON_STOP) viewModel.stopObservingRadio()
    }

    lifecycleOwner.lifecycle.addObserver(observer)

    onDispose {
      lifecycleOwner.lifecycle.removeObserver(observer)
    }
  }
}

@Composable
fun EditRadioScreen(
  onBackClicked: () -> Unit,
  onSaveClicked: (RadioInputWrapper) -> Unit,
  onPickImageClicked: ((Uri?) -> Unit) -> Unit,
  onErrorDismissed: () -> Unit,
  errorWidgetProvider: ErrorWidgetProvider,

  onSaved: (Long) -> Unit,

  modifier: Modifier = Modifier,
  radioToEdit: RadioPresentation? = null,
  isLoading: Boolean = false,
  savedRadioId: Long? = null,
  error: ErrorReference? = null
) {
  LaunchedEffect(key1 = savedRadioId) {
    if (savedRadioId != null) return@LaunchedEffect onSaved(savedRadioId) // todo: is it ok?
  }

  RadioScreenContent(
    onPickImageClicked = onPickImageClicked,
    onSaveClicked = onSaveClicked,
    onCancelClicked = onBackClicked,
    onErrorDismissed = onErrorDismissed,
    errorWidgetProvider = errorWidgetProvider,
    modifier = modifier,
    topAppBarData = RadioScreenTopAppBarData(
      stringResource(id = R.string.edit_radio_screen_label),
      isLoading,
      onBackClicked
    ),
    radioPresentation = radioToEdit,
    error = error,
    isLoading = isLoading
  )
}

@Preview
@Composable
fun EditRadioScreen() {
  val resources = LocalContext.current.resources
  val resourceId = R.drawable.space
  val coverUri = resources.getUriFromResource(resourceId)
  val state = EditRadioState(
    loadedRadio = RadioPresentation(
      0,
      "test title",
      "test description",
      coverUri,
      "http://url.com"
    )
  )

  val errorWidgetProvider = ErrorWidgetProvider()

  EditRadioScreen(
    onBackClicked = {  },
    onSaveClicked = {  },
    onPickImageClicked = {  },
    onErrorDismissed = {  },
    errorWidgetProvider = errorWidgetProvider,
    onSaved = {  },
    radioToEdit = state.loadedRadio
  )
}