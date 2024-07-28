package com.qubacy.hearit.application.ui.visual.controller.compose.screen.home

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import com.qubacy.hearit.R
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation

@Composable
fun RadioPlayer(
  radioPresentation: RadioPresentation,
  isPlaying: Boolean,
  modifier: Modifier = Modifier
) {
  val coverPainter =
    radioPresentation.cover?.let { rememberAsyncImagePainter(it) } ?:
    painterResource(id = R.drawable.radio_cover_placeholder)

  RadioPlayer(
    title = radioPresentation.title,
    isPlaying = isPlaying,
    coverPainter = coverPainter,
    modifier = modifier,
  )
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun RadioPlayer(
  title: String,
  isPlaying: Boolean,
  coverPainter: Painter,
  modifier: Modifier = Modifier
) {
  val normalGap = dimensionResource(id = R.dimen.gap_normal)
  val paddingVertical = dimensionResource(id = R.dimen.gep_small)

  ConstraintLayout(
    modifier = modifier.then(
      Modifier
        //.wrapContentHeight()
        .background(MaterialTheme.colorScheme.surface)
        .padding(vertical = paddingVertical, horizontal = normalGap)
    )
  ) {
    val coverContentDescription = stringResource(id = R.string.radio_player_cover_image_description)
    val prevButtonContentDescription = stringResource(id = R.string.radio_player_prev_button_description)
    val playButtonContentDescription = stringResource(id = R.string.radio_player_play_button_description)
    val nextButtonContentDescription = stringResource(id = R.string.radio_player_next_button_description)

    val playButtonIcon = AnimatedImageVector.animatedVectorResource(R.drawable.play_animated)

    val (coverRef, titleRef, prevButtonRef, playButtonRef, nextButtonRef) = createRefs()

    Image(
      painter = coverPainter,
      contentDescription = coverContentDescription,
      modifier = Modifier
        .constrainAs(coverRef) {
          start.linkTo(parent.start)
          top.linkTo(parent.top)
          bottom.linkTo(parent.bottom)

          height = Dimension.fillToConstraints
        }
    )

    Text(
      text = title,
      modifier = Modifier.constrainAs(titleRef) {
        top.linkTo(parent.top)
        bottom.linkTo(parent.bottom)
        start.linkTo(coverRef.end, normalGap)
        end.linkTo(prevButtonRef.start)
      }
    )

    IconButton(
      onClick = { /*TODO*/ },
      modifier = Modifier
        .semantics {
          contentDescription = prevButtonContentDescription
        }
        .constrainAs(prevButtonRef) {
          top.linkTo(parent.top)
          bottom.linkTo(parent.bottom)
          end.linkTo(playButtonRef.start, normalGap)
        }
    ) {
      Icon(
        painter = painterResource(id = R.drawable.prev_radio),
        contentDescription = ""
      )
    }

    IconButton(
      onClick = { /*TODO*/ },
      modifier = Modifier
        .semantics {
          contentDescription = playButtonContentDescription
        }
        .constrainAs(playButtonRef) {
          top.linkTo(parent.top)
          bottom.linkTo(parent.bottom)
          end.linkTo(nextButtonRef.start, normalGap)
        }
    ) {
      Icon(
        painter = rememberAnimatedVectorPainter(playButtonIcon, !isPlaying),
        contentDescription = ""
      )
    }

    IconButton(
      onClick = { /*TODO*/ },
      modifier = Modifier
        .semantics {
          contentDescription = nextButtonContentDescription
        }
        .constrainAs(nextButtonRef) {
          top.linkTo(parent.top)
          bottom.linkTo(parent.bottom)
          end.linkTo(parent.end)
        }
    ) {
      Icon(
        painter = painterResource(id = R.drawable.next_radio),
        contentDescription = ""
      )
    }
  }
}

@Preview
@Composable
fun RadioPlayer() {
  RadioPlayer(
    "test title",
    false,
    painterResource(id = R.drawable.radio_cover_placeholder),
    modifier = Modifier.fillMaxWidth()
  )
}