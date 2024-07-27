package com.qubacy.hearit.application.ui.visual.controller.compose.screen.home

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.qubacy.hearit.R
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.state.holder.home.HomeViewModel
import com.qubacy.hearit.application.ui.state.state.HomeState
import com.qubacy.hearit.application.ui.visual.resource.theme.HearItTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
  retrieveSavedRadioId: () -> Long?,
  onRadioLongPressed: (id: Long) -> Unit,
  onAddRadioClicked: () -> Unit,
  errorWidget: @Composable (
    ErrorReference,
    SnackbarHostState,
    CoroutineScope,
    onDismissRequested: () -> Unit
  ) -> Unit,

  modifier: Modifier = Modifier,
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
  viewModel: HomeViewModel = hiltViewModel()
) {
  val state: HomeState by (viewModel.state.observeAsState() as State<HomeState>)

  HomeScreen(
    retrieveAddedRadioId = retrieveSavedRadioId,
    onRadioPicked = { viewModel.setCurrentRadio(it) },
    onRadioLongPressed = onRadioLongPressed,
    onAddRadioClicked = onAddRadioClicked,
    onErrorDismissed = { viewModel.consumeCurrentError() },
    errorWidget = errorWidget,
    modifier = modifier,
    isLoading = state.isLoading,
    error = state.error,
    radioList = state.radioList ?: listOf()
  )

  SetupRadioListObserver(lifecycleOwner, viewModel)
}

@Composable
fun SetupRadioListObserver(lifecycleOwner: LifecycleOwner, viewModel: HomeViewModel) {
  DisposableEffect(key1 = lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
      if (event == Lifecycle.Event.ON_START) viewModel.observeRadioList()
      else if (event == Lifecycle.Event.ON_STOP) viewModel.stopObservingRadioList()
    }

    lifecycleOwner.lifecycle.addObserver(observer)

    onDispose {
      lifecycleOwner.lifecycle.removeObserver(observer)
    }
  }
}

@Composable
fun HomeScreen(
  retrieveAddedRadioId: () -> Long?,
  onRadioPicked: (id: Long) -> Unit,
  onRadioLongPressed: (id: Long) -> Unit,
  onAddRadioClicked: () -> Unit,
  onErrorDismissed: () -> Unit,
  errorWidget: @Composable (
    ErrorReference,
    SnackbarHostState,
    CoroutineScope,
    () -> Unit
  ) -> Unit,

  modifier: Modifier = Modifier,
  isLoading: Boolean = false,
  error: ErrorReference? = null,
  radioList: List<RadioPresentation> = listOf(),
  currentRadioPresentation: RadioPresentation? = null,
  isRadioPlaying: Boolean = false
) {
  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()

  val fabDescription = stringResource(id = R.string.home_screen_fab_description)
  val radioListDescription = stringResource(id = R.string.home_screen_radio_list_description)
  val radioListItemDescriptionTemplate = stringResource(
    id = R.string.home_screen_radio_list_item_description_template
  )
  
  Scaffold(
    modifier = modifier,
    topBar = { HomeAppBar(isLoading) },
    snackbarHost = {
      SnackbarHost(hostState = snackbarHostState)
    },
    floatingActionButton = {
      FloatingActionButton(
        modifier = Modifier.semantics { 
          contentDescription = fabDescription
        },
        onClick = onAddRadioClicked
      ) {
        Icon(
          imageVector = Icons.Rounded.Add,
          contentDescription = "Add radio button"
        )
      }
    }
  ) { contentPadding ->
    Column(modifier = Modifier.padding(contentPadding)) {
      LazyColumn(
        modifier = Modifier
          .semantics {
            contentDescription = radioListDescription
          }
          .fillMaxWidth()
          .fillMaxHeight(1f)
      ) {
        itemsIndexed(radioList, key = { _: Int, item: RadioPresentation ->
          item.id
        }) { index, item ->
          RadioListItem(
            item,
            { onRadioLongPressed(item.id) },
            { onRadioPicked(item.id) },
            modifier = Modifier.semantics {
              contentDescription = radioListItemDescriptionTemplate.format(item.id)
            }
          )

          if (index != radioList.size - 1) HorizontalDivider()
        }
      }

      if (currentRadioPresentation != null) {
        RadioPlayer(
          radioPresentation = currentRadioPresentation,
          isPlaying = isRadioPlaying,
          modifier = Modifier
            .semantics {
              contentDescription = "" // todo: set an actual value;
            }
            .fillMaxWidth()
            .wrapContentHeight()
        )
      }
    }
  }

  retrieveAddedRadioId()?.let {
    showSavedRadioSnackbar(snackbarHostState, LocalContext.current, coroutineScope)
  }
  error?.let {
    errorWidget(it, snackbarHostState, coroutineScope, onErrorDismissed)
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(
  isLoading: Boolean,
  modifier: Modifier = Modifier
) {
  val topAppBarDescription = stringResource(id = R.string.home_screen_top_app_bar_description)
  val loadingIndicatorDescription = stringResource(id = R.string.home_screen_loading_indicator_description)

  Box {
    TopAppBar(
      modifier = modifier.then(Modifier.semantics {
        contentDescription = topAppBarDescription
      }),
      title = {
        Text(
          text = stringResource(id = R.string.home_screen_top_app_bar_title),
          style = MaterialTheme.typography.titleLarge
        )
      }
    )

    if (isLoading)
      LinearProgressIndicator(
        modifier = Modifier
          .semantics {
            contentDescription = loadingIndicatorDescription
          }
          .align(Alignment.BottomStart)
          .fillMaxWidth()
      )
  }
}

private fun showSavedRadioSnackbar(
  snackbarHostState: SnackbarHostState,
  context: Context,
  coroutineScope: CoroutineScope
) {
  coroutineScope.launch {
    snackbarHostState.showSnackbar(
      context.getString(R.string.home_screen_radio_added_snackbar_message)
    )
  }
}

@Preview
@Composable
fun HomeScreen() {
  val radioList = IntRange(0, 20).map { index ->
    RadioPresentation(
      index.toLong(),
      "test $index",
      "test description",
      null, "http://url"
    )
  }

  HearItTheme {
    HomeScreen(
      retrieveAddedRadioId = { 0 },
      onRadioPicked = {  },
      onRadioLongPressed = {  },
      onAddRadioClicked = {  },
      onErrorDismissed = {  },
      errorWidget = { _, _, _, _ -> },
      radioList = radioList
    )
  }
}

@Deprecated("Can't be used in @PreviewParameter for some reason")
private class RadioListPreviewParamProvider : PreviewParameterProvider<List<RadioPresentation>> {
  override val values: Sequence<List<RadioPresentation>> = sequenceOf(
    listOf(),
    listOf(
      RadioPresentation(0, "test 1", "test description", null, "http://url.com/0"),
      RadioPresentation(1, "test 2", "test description", null, "http://url.com/0"),
      RadioPresentation(2, "test 3", "test description", null, "http://url.com/0")
    )
  )
}