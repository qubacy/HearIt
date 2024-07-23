package com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.qubacy.hearit.R
import com.qubacy.hearit.application._common.resources.util.getUriFromResource
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.state.holder.radio.EditRadioViewModel
import com.qubacy.hearit.application.ui.state.state.EditRadioState
import com.qubacy.hearit.application.ui.visual.controller.activity._common.aspect.ImagePickerActivity
import com.qubacy.hearit.application.ui.visual.controller.activity._common.util.findActivity

@Composable
fun EditRadioScreen(
  onBackClicked: () -> Unit,
  onSaveClicked: (Long) -> Unit,

  modifier: Modifier = Modifier,
  viewModel: EditRadioViewModel = hiltViewModel()
) {
  val state by viewModel.state.observeAsState()

  EditRadioScreen(
    state!!,
    onBackClicked,
    { radioToSave: RadioPresentation -> viewModel.saveRadio(radioToSave) },
    onSaveClicked,
    modifier
  )
}

@Composable
fun EditRadioScreen(
  state: EditRadioState,

  onBackClicked: () -> Unit,
  onSaveClicked: (RadioPresentation) -> Unit,

  onSaved: (Long) -> Unit,

  modifier: Modifier = Modifier
) {
  if (state is EditRadioState.Success) return onSaved(state.radio.id) // todo: is it ok?

  val context = LocalContext.current

  RadioScreenContent(
    topAppBarData = RadioScreenTopAppBarData(
      stringResource(id = R.string.edit_radio_screen_label),
      (state is EditRadioState.Loading),
      onBackClicked
    ),
    onPickImageClicked = { onPicked -> pickImage(context, onPicked) },
    onSaveClicked = onSaveClicked,
    onCancelClicked = onBackClicked,
    modifier = modifier,
    radioPresentation = if (state is EditRadioState.Loaded) state.radio else null
  )
}

private fun pickImage(context: Context, onPicked: (Uri?) -> Unit) {
  val activity = context.findActivity()!!

  if (activity !is ImagePickerActivity) return

  activity.pickImage { uri ->
    onPicked(uri)
  }
}

@Preview
@Composable
fun EditRadioScreen() {
  val resources = LocalContext.current.resources
  val resourceId = R.drawable.space
  val coverUri = resources.getUriFromResource(resourceId)

  EditRadioScreen(
    state = EditRadioState.Loaded(
      RadioPresentation(
        0,
        "test title",
        "test description",
        coverUri
      )
    ),
    onBackClicked = { /*TODO*/ },
    onSaveClicked = { },
    onSaved = { }
  )
}