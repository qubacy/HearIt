package com.qubacy.hearit.application.ui.visual.controller.compose.screen.home

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import com.qubacy.hearit.R
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation

@Composable
fun RadioPlayer(
  radioPresentation: RadioPresentation,
  isPlaying: Boolean,
  isExpanded: Boolean,
  modifier: Modifier = Modifier
) {
  val coverPainter =
    radioPresentation.cover?.let { rememberAsyncImagePainter(it) } ?:
    painterResource(id = R.drawable.radio_cover_placeholder)

  RadioPlayer(
    title = radioPresentation.title,
    description = radioPresentation.description,
    isPlaying = isPlaying,
    isExpanded = isExpanded,
    coverPainter = coverPainter,
    modifier = modifier,
  )
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun RadioPlayer(
  title: String,
  description: String?,
  isPlaying: Boolean,
  isExpanded: Boolean,
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

    val (coverRef, titleRef, descriptionRef, prevButtonRef, playButtonRef, nextButtonRef) = createRefs()

    Image(
      painter = coverPainter,
      contentDescription = coverContentDescription,
      contentScale = if (isExpanded) ContentScale.Crop else ContentScale.Fit,
      modifier = Modifier
        .clip(AbsoluteRoundedCornerShape(8.dp))
        .background(Color.Blue) // todo: delete;
        .constrainAs(coverRef) {
          top.linkTo(parent.top)

          if (!isExpanded) {
            start.linkTo(parent.start)
            bottom.linkTo(parent.bottom)
          } else {
            bottom.linkTo(titleRef.top)

            width = Dimension.matchParent
          }

          height = Dimension.fillToConstraints
        }
    )

    Text(
      text = title,
      modifier = Modifier.constrainAs(titleRef) {
        if (!isExpanded) {
          top.linkTo(parent.top)
          bottom.linkTo(parent.bottom)
          start.linkTo(coverRef.end, normalGap)
          end.linkTo(prevButtonRef.start)
        } else {
          top.linkTo(coverRef.bottom, normalGap)
          bottom.linkTo(if (description != null) descriptionRef.top else playButtonRef.top)
          start.linkTo(parent.start)
        }
      }
    )

    if (isExpanded && description != null) {
      Text(
        text = description,
        modifier = Modifier.constrainAs(descriptionRef) {
          top.linkTo(titleRef.bottom, normalGap)
          start.linkTo(parent.start)
        }
      )
    }

    IconButton(
      onClick = { /*TODO*/ },
      modifier = Modifier
        .semantics {
          contentDescription = prevButtonContentDescription
        }
        .constrainAs(prevButtonRef) {
          if (!isExpanded) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            end.linkTo(playButtonRef.start, normalGap)
          } else {
            top.linkTo(playButtonRef.top)
            end.linkTo(playButtonRef.start)
          }
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
          if (!isExpanded) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            end.linkTo(nextButtonRef.start, normalGap)
          } else {
            top.linkTo(if (description != null) descriptionRef.bottom else titleRef.bottom, normalGap)
            bottom.linkTo(parent.bottom)

            centerHorizontallyTo(parent)
          }
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
          if (!isExpanded) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            end.linkTo(parent.end)
          } else {
            top.linkTo(playButtonRef.top)
            start.linkTo(playButtonRef.end)
          }
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
    null,
    false,
    true,
    painterResource(id = R.drawable.radio_cover_placeholder),
    modifier = Modifier.fillMaxWidth()
  )
}