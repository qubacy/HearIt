package com.qubacy.hearit.application.ui.visual.controller.compose.screen.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.qubacy.hearit.R
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application._common.player.packet.PlayerStatePacketBody
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.state.holder.home.HomeViewModel
import com.qubacy.hearit.application.ui.state.state.home.HomeState
import com.qubacy.hearit.application.ui.state.state.home.player.PlayerState
import com.qubacy.hearit.application.ui.visual.controller.activity._common.aspect.PlayerActivity
import com.qubacy.hearit.application.ui.visual.controller.activity._common.util.findActivity
import com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.components.error.provider.ErrorWidgetProvider
import com.qubacy.hearit.application.ui.visual.resource.theme.HearItTheme
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
  retrieveSavedRadioId: () -> Long?,
  onRadioLongPressed: (id: Long) -> Unit,
  onAddRadioClicked: () -> Unit,
  errorWidgetProvider: ErrorWidgetProvider,

  modifier: Modifier = Modifier,
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
  viewModel: HomeViewModel = hiltViewModel()
) {
  val state: HomeState by (viewModel.state.observeAsState() as State<HomeState>)
  val playerState: PlayerState by (viewModel.playerState.observeAsState() as State<PlayerState>)
  // todo: seems like a bad architectural decision:
  val playerStatePacketBodyToSend: PlayerStatePacketBody?
    by (viewModel.playerStatePacketBody.observeAsState())

  var areCoversPermissionsDemanded by remember { mutableStateOf<Boolean?>(null) }
  var isPlayerExpanded by remember { mutableStateOf(false) }
  val currentRadioPresentation = playerState.currentRadio

  val playerActivity = LocalContext.current.findActivity() as PlayerActivity

  LaunchedEffect(key1 = Unit) {
    playerActivity.setPlayerActivityCallback(object : PlayerActivity.Callback {
      override fun onPlayerStatePacketGotten(playerStatePacketBody: PlayerStatePacketBody) {
        viewModel.resolvePlayerStatePacketBody(playerStatePacketBody)
      }

      override fun onCoversPermissionsGranted() {
        areCoversPermissionsDemanded = false
      }
    })
  }

  if (areCoversPermissionsDemanded == null && state.radioList != null)
    areCoversPermissionsDemanded = true

  LaunchedEffect(key1 = areCoversPermissionsDemanded) {
    if (areCoversPermissionsDemanded != true) return@LaunchedEffect

    playerActivity.demandCoversPermissions(state.radioList!!.mapNotNull { it.cover })
  }
  
  LaunchedEffect(key1 = playerStatePacketBodyToSend) {
    // todo: is it for real?:
    if (playerStatePacketBodyToSend == null) return@LaunchedEffect

    playerActivity.setPlayerState(playerStatePacketBodyToSend!!)
  }

  HomeScreen(
    retrieveAddedRadioId = retrieveSavedRadioId,
    onRadioPicked = { viewModel.setCurrentRadio(it) },
    onRadioLongPressed = onRadioLongPressed,
    onAddRadioClicked = onAddRadioClicked,
    onErrorDismissed = { viewModel.consumeCurrentError() },
    onPlayerClicked = { isPlayerExpanded = true },
    onPlayerBackgroundClicked = { isPlayerExpanded = false },
    radioPlayerCallback = RadioPlayerCallback({
        viewModel.setPrevRadio()
      }, {
        if (playerState.currentRadio == null) return@RadioPlayerCallback

        viewModel.changePlayingState()
      }, {
        viewModel.setNextRadio()
      }),
    errorWidgetProvider = errorWidgetProvider,
    modifier = modifier,
    isLoading = state.isLoading,
    error = state.error,
    radioList = state.radioList ?: listOf(),
    currentRadioPresentation = currentRadioPresentation,
    isRadioPlaying = playerState.isRadioPlaying,
    isPlayerExpanded = isPlayerExpanded,
    areCoversPrepared = areCoversPermissionsDemanded == false
  )

  SetupRadioListObserver(lifecycleOwner, viewModel)
}

@Composable
fun SetupRadioListObserver(lifecycleOwner: LifecycleOwner, viewModel: HomeViewModel) {
  DisposableEffect(key1 = lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
      if (event == Lifecycle.Event.ON_START) {
        viewModel.observeRadioList()
      }
      else if (event == Lifecycle.Event.ON_STOP) {
        viewModel.stopObservingRadioList()
      }
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
  onPlayerClicked: () -> Unit,
  onPlayerBackgroundClicked: () -> Unit,
  radioPlayerCallback: RadioPlayerCallback,
  errorWidgetProvider: ErrorWidgetProvider,

  modifier: Modifier = Modifier,
  isLoading: Boolean = false,
  error: ErrorReference? = null,
  radioList: List<RadioPresentation> = listOf(),
  currentRadioPresentation: RadioPresentation? = null,
  isRadioPlaying: Boolean = false,
  isPlayerExpanded: Boolean = false,
  areCoversPrepared: Boolean = false
) {
  val fabDescription = stringResource(id = R.string.home_screen_fab_description)
  val radioListDescription = stringResource(id = R.string.home_screen_radio_list_description)
  val radioListItemDescriptionTemplate = stringResource(
    id = R.string.home_screen_radio_list_item_description_template
  )
  val radioPlayerDescription = stringResource(id = R.string.home_screen_radio_player_description)
  val radioPlayerBackgroundDescription = stringResource(
    id = R.string.home_screen_radio_player_background_description
  )
  
  Scaffold(
    modifier = modifier,
    topBar = { HomeAppBar(isLoading) }
  ) { contentPadding ->
    ConstraintLayout(
      modifier = Modifier
        .fillMaxSize()
        .padding(contentPadding)
    ) {
      val isPlayerVisible = currentRadioPresentation != null
      var finalCurrentRadioPresentation by remember { mutableStateOf(currentRadioPresentation) }

      val normalGap = dimensionResource(id = R.dimen.gap_normal)

      val (listRef, fabRef, playerWrapperRef, playerScrimRef, snackbarRef, errorSnackbarRef) =
        createRefs()

      val guidelineVertical50Ref = createGuidelineFromTop(0.5f)

      if (areCoversPrepared) {
        LazyColumn(
          modifier = Modifier
            .semantics {
              contentDescription = radioListDescription
            }
            .constrainAs(listRef) {
              top.linkTo(parent.top)
              bottom.linkTo(
                if ((isPlayerVisible || finalCurrentRadioPresentation != null) && !isPlayerExpanded)
                  playerWrapperRef.top
                else parent.bottom
              )

              height = Dimension.fillToConstraints
              width = Dimension.matchParent
            }
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
              },
              enabled = !isLoading
            )

            if (index != radioList.size - 1) HorizontalDivider()
          }
        }
      }

      SavedRadioSnackbar(
        retrieveAddedRadioId(),
        modifier = Modifier.constrainAs(snackbarRef) {
          bottom.linkTo(fabRef.top)
          start.linkTo(parent.start)
        }
      )

      FloatingActionButton(
        modifier = Modifier
          .constrainAs(fabRef) {
            end.linkTo(parent.end, normalGap)

            if (!isPlayerVisible || isPlayerExpanded) {

              bottom.linkTo(parent.bottom, normalGap)
            } else {
              bottom.linkTo(playerWrapperRef.top, normalGap)
            }
          }
          .semantics {
            contentDescription = fabDescription
          },
        onClick = onAddRadioClicked // seems like FAB should always be enabled;
      ) {
        Icon(
          imageVector = Icons.Rounded.Add,
          contentDescription = "Add radio button"
        )
      }

      val playerAnimatedOffset by animateOffsetAsState(
        targetValue = Offset(0f, if (isPlayerVisible) 0f else 1f)
      ) {
        finalCurrentRadioPresentation = currentRadioPresentation
      }

      if (finalCurrentRadioPresentation != null && currentRadioPresentation != null
        && finalCurrentRadioPresentation != currentRadioPresentation
      ) {
        finalCurrentRadioPresentation = currentRadioPresentation
      }

      if (isPlayerVisible || finalCurrentRadioPresentation != null) {
        val visibleCurrentRadioPresentation = currentRadioPresentation ?: finalCurrentRadioPresentation!!

        val scrimAnimatedColor by animateColorAsState(
          targetValue =
            if (!isPlayerExpanded) Color.Transparent
            else MaterialTheme.colorScheme.scrim.copy(alpha = 0.3f)
        )

        Box(
          modifier = Modifier
            .semantics {
              contentDescription = radioPlayerBackgroundDescription
            }
            .background(scrimAnimatedColor)
            .constrainAs(playerScrimRef) {
              top.linkTo(if (!isPlayerExpanded) parent.bottom else parent.top)
              bottom.linkTo(parent.bottom)

              width = Dimension.matchParent
              height = Dimension.fillToConstraints
            }
            .let { return@let if (!isLoading) it.clickable { onPlayerBackgroundClicked() } else it }
        ) {}

        RadioPlayer(
          radioPresentation = visibleCurrentRadioPresentation,
          isPlaying = isRadioPlaying,
          isExpanded = isPlayerExpanded,
          callback = radioPlayerCallback,
          modifier = Modifier
            .let {
              if (!isPlayerExpanded) return@let it.wrapContentHeight()

              it
            }
            .semantics {
              contentDescription = radioPlayerDescription
            }
            .offset(0.dp, playerAnimatedOffset.y.dp)
            .constrainAs(playerWrapperRef) {
              if (isPlayerExpanded) top.linkTo(guidelineVertical50Ref)
              bottom.linkTo(parent.bottom)

              width = Dimension.matchParent
              if (isPlayerExpanded) height = Dimension.fillToConstraints
            }
            .let {
              if (!isPlayerExpanded && !isLoading) return@let it.clickable { onPlayerClicked() }

              it
            },
            enabled = !isLoading
        )
      }

      error?.let {
        errorWidgetProvider.ErrorWidget(
          error = it,
          showSnackbarManually = true,
          onNotCriticalDismissRequest = onErrorDismissed,
          snackbarModifier = Modifier.constrainAs(errorSnackbarRef) {
            if (!isPlayerExpanded) bottom.linkTo(fabRef.top, normalGap)
            else bottom.linkTo(playerWrapperRef.top, normalGap)

            start.linkTo(parent.start)
          }
        )
      }
    }
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

@Composable
fun SavedRadioSnackbar(
  radioId: Long?,
  modifier: Modifier = Modifier
) {
  val snackbarCoroutineScope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }

  SnackbarHost(
    hostState = snackbarHostState,
    modifier = modifier
  )

  radioId?.let {
    val context = LocalContext.current

    snackbarCoroutineScope.launch {
      snackbarHostState.showSnackbar(
        context.getString(R.string.home_screen_radio_added_snackbar_message)
      )
    }
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

  var curRadio: RadioPresentation? by remember {
    mutableStateOf(null)
  }

  val errorWidgetProvider = ErrorWidgetProvider()

  HearItTheme {
    HomeScreen(
      retrieveAddedRadioId = { 0 },
      onRadioPicked = { id -> curRadio = radioList.find { it.id == id } },
      onRadioLongPressed = { curRadio = null },
      onAddRadioClicked = {  },
      onErrorDismissed = {  },
      onPlayerClicked = {  },
      onPlayerBackgroundClicked = {  },
      radioPlayerCallback = RadioPlayerCallback({}, {}, {}),
      errorWidgetProvider = errorWidgetProvider,
      radioList = radioList,
      currentRadioPresentation = curRadio,
      isRadioPlaying = false,
      isPlayerExpanded = false,
      isLoading = false
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