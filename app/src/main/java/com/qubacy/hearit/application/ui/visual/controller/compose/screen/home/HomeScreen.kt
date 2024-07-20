package com.qubacy.hearit.application.ui.visual.controller.compose.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.qubacy.hearit.R
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.state.holder.home.HomeViewModel
import com.qubacy.hearit.application.ui.state.state.HomeState
import com.qubacy.hearit.application.ui.visual.resource.theme.HearItTheme

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
  Scaffold(
    modifier = modifier,
    topBar = { HomeAppBar() },
    floatingActionButton = {
      FloatingActionButton(
        onClick = onAddRadioClicked
      ) {
        Icon(
          imageVector = Icons.Rounded.Add,
          contentDescription = "Add radio button"
        )
      }
    }
  ) { contentPadding ->
    // todo: think twice. got to do this dirt cause of the edge effect problems:
    Box(modifier = Modifier.padding(contentPadding)) {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
      ) {
        if (state !is HomeState.Success) return@LazyColumn

        itemsIndexed(state.radioList, key = { _: Int, item: RadioPresentation ->
          item.id
        }) { index, item ->
          RadioListItem(item, { onRadioClicked(item.id) })

          if (index != state.radioList.size - 1) HorizontalDivider()
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(
  modifier: Modifier = Modifier
) {
  TopAppBar(
    modifier = modifier,
    title = {
      Text(
        text = stringResource(id = R.string.home_top_app_bar_title),
        style = MaterialTheme.typography.titleLarge
      )
    }
  )
}

@Preview
@Composable
fun HomeScreen() {
  val radioList = IntRange(0, 20).map { index ->
    RadioPresentation(index.toLong(), "test $index", "test description")
  }

  HearItTheme {
    HomeScreen(
      state = HomeState.Success(radioList),
      onRadioClicked = {  },
      onAddRadioClicked = { /*TODO*/ }
    )
  }
}

@Deprecated("Can't be used in @PreviewParameter for some reason")
private class RadioListPreviewParamProvider : PreviewParameterProvider<List<RadioPresentation>> {
  override val values: Sequence<List<RadioPresentation>> = sequenceOf(
    listOf(),
    listOf(
      RadioPresentation(0, "test 1", "test description"),
      RadioPresentation(1, "test 2", "test description"),
      RadioPresentation(2, "test 3", "test description")
    )
  )
}