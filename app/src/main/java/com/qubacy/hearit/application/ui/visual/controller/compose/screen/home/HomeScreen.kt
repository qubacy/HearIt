package com.qubacy.hearit.application.ui.visual.controller.compose.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.qubacy.hearit.application.ui.state.holder.home.HomeViewModel
import com.qubacy.hearit.application.ui.state.state.HomeState

@Composable
fun HomeScreen(
  onRadioClicked: (id: Long) -> Unit,
  onAddRadioClicked: () -> Unit,

  modifier: Modifier = Modifier,
  viewModel: HomeViewModel = hiltViewModel()
) {
  val state by viewModel.state.observeAsState()

  HomeScreen(
    state = state!!,
    onRadioClicked = onRadioClicked,
    onAddRadioClicked = onAddRadioClicked,
    modifier = modifier
  )
}

@Composable
fun HomeScreen(
  state: HomeState,

  onRadioClicked: (id: Long) -> Unit,
  onAddRadioClicked: () -> Unit,

  modifier: Modifier = Modifier
) {

}