package com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.qubacy.hearit.application.ui.state.holder.radio.AddRadioViewModel

const val ADDED_RADIO_ID_RESULT_KEY = "addedRadioId"

@Composable
fun AddRadioScreen(
  onBackClicked: () -> Unit,
  onCreateClicked: (Long) -> Unit,

  modifier: Modifier = Modifier,
  viewModel: AddRadioViewModel = hiltViewModel()
) {

}