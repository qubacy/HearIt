package com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.state.holder.radio.EditRadioViewModel
import com.qubacy.hearit.application.ui.state.state.EditRadioState

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

  // todo: implement a layout..


}