package com.qubacy.hearit.application.ui.visual.controller.compose.screen.home

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.qubacy.hearit.R
import com.qubacy.hearit.application._common.resources.util.getUriFromResource
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RadioListItem(
  radioPresentation: RadioPresentation,
  onRadioLongPressed: (Long) -> Unit,
  onRadioClicked: (Long) -> Unit,
  modifier: Modifier = Modifier
) {
  ConstraintLayout(
    modifier = modifier.then(
      Modifier
        .combinedClickable(
          onClick = { onRadioClicked(radioPresentation.id) },
          onLongClick = { onRadioLongPressed(radioPresentation.id) }
        )
        .height(IntrinsicSize.Min)
        .padding(
          horizontal = dimensionResource(id = R.dimen.gap_normal),
          vertical = dimensionResource(id = R.dimen.gep_small)
        )
    )
  ) {
    val (coverRef, titleRef, descRef) = createRefs()

    val coverModifier = Modifier.constrainAs(coverRef) {
      top.linkTo(parent.top)
      start.linkTo(parent.start)
      bottom.linkTo(parent.bottom)

      height = Dimension.fillToConstraints
    }.clip(AbsoluteRoundedCornerShape(4.dp))

    if (radioPresentation.cover != null)
      RadioListItemCover(uri = radioPresentation.cover, coverModifier)
    else
      RadioListItemCoverPlaceholder(coverModifier)

    val gapSize = dimensionResource(id = R.dimen.gap_normal)

    RadioListItemTitle(
      radioPresentation.title,
      modifier = Modifier.constrainAs(titleRef) {
        top.linkTo(parent.top)
        start.linkTo(coverRef.end, margin = gapSize)

        width = Dimension.fillToConstraints
      }
    )

    RadioListItemDescription(
      radioPresentation.description,
      modifier = Modifier.constrainAs(descRef) {
        top.linkTo(titleRef.bottom)
        start.linkTo(titleRef.start)

        width = Dimension.fillToConstraints
      }
    )
  }
}

@Composable
fun RadioListItemCover(
  uri: Uri,
  modifier: Modifier = Modifier
) {
  AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
      .data(uri)
      .crossfade(true)
      .build(),
    // todo: come up with an idea how to tint this correctly:
    placeholder = painterResource(R.drawable.home_radio_list_item_cover_placeholder),
    contentDescription = stringResource(R.string.home_screen_radio_list_item_cover_content_description),
    contentScale = ContentScale.Fit,
    modifier = modifier
  )
}

@Composable
fun RadioListItemCoverPlaceholder(
  modifier: Modifier = Modifier
) {
  Icon(
    painter = painterResource(R.drawable.home_radio_list_item_cover_placeholder),
    contentDescription = stringResource(R.string.home_screen_radio_list_item_cover_content_description),
    tint = MaterialTheme.colorScheme.onSurfaceVariant,
    modifier = modifier
  )
}

@Composable
fun RadioListItemTitle(
  title: String,
  modifier: Modifier = Modifier
) {
  Text(
    text = title,
    textAlign = TextAlign.Start,
    style = MaterialTheme.typography.bodyLarge,
    modifier = modifier,
    overflow = TextOverflow.Ellipsis
  )
}

@Composable
fun RadioListItemDescription(
  description: String?,
  modifier: Modifier = Modifier
) {
  Text(
    text = description ?: String(),
    textAlign = TextAlign.Start,
    style = MaterialTheme.typography.bodyMedium,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
    modifier = modifier,
    overflow = TextOverflow.Ellipsis
  )
}

@Preview
@Composable
fun RadioListItem() {
  val resources = LocalContext.current.resources
  val resourceId = R.drawable.ic_launcher_background
  val coverUri = resources.getUriFromResource(resourceId)
  val radioPresentation = RadioPresentation(
    0, "title", "desc", coverUri, "http://url.com"
  )

  RadioListItem(
    radioPresentation,
    { id -> println("RadioListItem(): clicked $id;") },
    { id -> println("RadioListItem(): clicked $id;") }
  )
}