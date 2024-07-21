package com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.qubacy.hearit.R
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.visual.resource.theme.HearItTheme

@Composable
fun RadioScreenContent(
    topAppBar: @Composable (isLoading: Boolean) -> Unit,

    onSaveClicked: (RadioPresentation) -> Unit,
    onCancelClicked: () -> Unit,

    modifier: Modifier = Modifier,

    isLoading: Boolean = false,
    radioPresentation: RadioPresentation? = null
    ) {
    Scaffold(
        topBar = { topAppBar(isLoading) }
    ) { paddingValues ->
        val normalGap = dimensionResource(id = R.dimen.gap_normal)

        ConstraintLayout(
            modifier = modifier.then(
                Modifier
                    .padding(paddingValues)
                    .padding(normalGap)
                    .fillMaxSize())
        ) {
            var title by remember { mutableStateOf(radioPresentation?.title ?: "") }
            var description by remember { mutableStateOf(radioPresentation?.description ?: "") }
            var imageUri: Uri? by remember { mutableStateOf(radioPresentation?.cover) }

            val (
                titleRef,
                descriptionRef,
                imageIconRef,
                imageHintRef,
                imageButtonRef,
                imagePreviewRef,
                saveButtonRef,
                cancelButtonRef
            ) = createRefs()

            RadioScreenTextInput(
                value = title,
                onValueChanged = { title = it },
                R.drawable.letter_t,
                R.string.radio_screen_content_title_input_icon_description,
                R.string.radio_screen_content_title_input_label,
                modifier = Modifier.constrainAs(titleRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)

                    width = Dimension.matchParent
                }
            )

            RadioScreenTextInput(
                value = description,
                onValueChanged = { description = it },
                R.drawable.paragraph,
                R.string.radio_screen_content_description_input_icon_description,
                R.string.radio_screen_content_description_input_label,
                modifier = Modifier.constrainAs(descriptionRef) {
                    top.linkTo(titleRef.bottom)
                    start.linkTo(parent.start)

                    width = Dimension.matchParent
                }
            )

            RadioScreenIcon(
                drawableRes = R.drawable.image,
                contentDescriptionRes = R.string.radio_screen_content_image_input_icon_description,
                modifier = Modifier.constrainAs(imageIconRef) {
                    top.linkTo(descriptionRef.bottom, normalGap)
                    start.linkTo(parent.start)
                }
            )

            Text(
                text = stringResource(id = R.string.radio_screen_content_image_input_hint),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.constrainAs(imageHintRef) {
                    top.linkTo(imageIconRef.top)
                    start.linkTo(imageIconRef.end, normalGap)
                    end.linkTo(imageButtonRef.start)

                    width = Dimension.fillToConstraints
                }
            )

            RadioScreenImageButton(
                onClick = {
                    // todo: call for an image picker..


                },
                modifier = Modifier.constrainAs(imageButtonRef) {
                    top.linkTo(imageIconRef.top)
                    start.linkTo(imageHintRef.end)
                    end.linkTo(parent.end)
                }
            )

            RadioScreenImagePreview(
                imageUri = imageUri,
                onCloseClicked = {
                    imageUri = null
                },
                modifier = Modifier.constrainAs(imagePreviewRef) {
                    top.linkTo(imageButtonRef.bottom)
                    start.linkTo(parent.start)
                    bottom.linkTo(saveButtonRef.top, normalGap)

                    width = Dimension.matchParent
                    height = Dimension.fillToConstraints
                }
            )

            Button(
                onClick = {
                    onSaveClicked(
                        RadioPresentation(
                            id = radioPresentation?.id ?: RadioPresentation.UNSPECIFIED_ID,
                            title = title,
                            description = description.ifEmpty { null },
                            cover = imageUri
                        )
                    )
                },
                modifier = Modifier.constrainAs(saveButtonRef) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
            ) {
                Text(
                    text = stringResource(id = R.string.radio_screen_content_save_button_text)
                )
            }

            OutlinedButton(
                onClick = onCancelClicked,
                modifier = Modifier.constrainAs(cancelButtonRef) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(saveButtonRef.start, normalGap)
                }
            ) {
                Text(
                    text = stringResource(id = R.string.radio_screen_content_cancel_button_text)
                )
            }
        }
    }
}

@Composable
fun RadioScreenTextInput(
    value: String,
    onValueChanged: (String) -> Unit,
    @DrawableRes iconDrawableRes: Int,
    @StringRes iconContentDescriptionRes: Int,
    @StringRes labelRes: Int,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        leadingIcon = {
            RadioScreenIcon(
                drawableRes = iconDrawableRes,
                contentDescriptionRes = iconContentDescriptionRes
            )
        },
        label = { Text(text = stringResource(id = labelRes)) },
        modifier = modifier
    )
}

@Composable
fun RadioScreenIcon(
    @DrawableRes drawableRes: Int,
    @StringRes contentDescriptionRes: Int,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = drawableRes),
        contentDescription = stringResource(id = contentDescriptionRes),
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
        modifier = modifier.then(
            Modifier
                .size(dimensionResource(id = R.dimen.icon_size_normal))
                .padding(dimensionResource(id = R.dimen.icon_padding_normal))
        )
    )
}

@Composable
fun RadioScreenImageButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.more),
            contentDescription = stringResource(
                id = R.string.radio_screen_content_image_input_choose_button_icon_description
            ),
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.icon_size_normal))
        )
    }
}

fun Modifier.scrollableContent(offset: IntOffset) =
    this then OffsetModifierElement(offset)

private class OffsetModifierElement(
    val offset: IntOffset
) : ModifierNodeElement<OffsetModifierNode>() {
    override fun create(): OffsetModifierNode {
        return OffsetModifierNode(offset)
    }

    override fun equals(other: Any?): Boolean {
        val otherModifierElement = other as? OffsetModifierElement ?: return false

        return offset == otherModifierElement.offset
    }

    override fun hashCode(): Int {
        return offset.hashCode()
    }

    override fun update(node: OffsetModifierNode) {
        Log.d("OffsetModifierElement", "update(): $offset;")

        node.offset = offset
    }
}

private class OffsetModifierNode(
    var offset: IntOffset
) : LayoutModifierNode, Modifier.Node() {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(constraints)

        return layout(placeable.width, placeable.height) {
            val heightDelta = placeable.height -
            val yWithOffset =
                if (heightDelta <= 0 || offset.y <= 0) 0
                else if (heightDelta - offset.y >= 0) offset.y
                else 0

            Log.d("OffsetModifierNode", "measure(): heightDelta = $heightDelta; width = ${placeable.width}; height = ${placeable.height}; yWithOffset = $yWithOffset;")

            placeable.placeRelative(0, yWithOffset)
        }
    }
}

@Composable
fun RadioScreenImagePreview(
    imageUri: Uri?,
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var offset by remember { mutableFloatStateOf(0f) }

    AnimatedVisibility(
        visible = imageUri != null,
        modifier = modifier.then(
            Modifier.scrollable(state = rememberScrollableState { distance ->
                offset += distance

                distance
            }, Orientation.Vertical).clipToBounds()
        )
    ) {
        Box(
            modifier = Modifier.wrapContentHeight(align = Alignment.Top, unbounded = true)
        ) {
            AsyncImage(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(imageUri)
                    .build(),
                contentDescription = stringResource(
                    id = R.string.radio_screen_content_image_input_preview_image_description
                ),
                contentScale = ContentScale.None,
                modifier = Modifier
                    .scrollableContent(IntOffset(0, offset.toInt()))
                    .clip(AbsoluteRoundedCornerShape(12.dp))
            )

            val buttonMargin = dimensionResource(id = R.dimen.gap_small_less)

            FilledTonalIconButton(
                onClick = onCloseClicked,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(-buttonMargin, buttonMargin)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = stringResource(
                        id = R.string.radio_screen_content_image_input_preview_close_button_description
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RadioScreenContent() {
    val resources = LocalContext.current.resources
    val resourceId = R.drawable.space
    val coverUri = Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(resources.getResourcePackageName(resourceId))
        .appendPath(resources.getResourceTypeName(resourceId))
        .appendPath(resources.getResourceEntryName(resourceId))
        .build()

    HearItTheme {
        RadioScreenContent(
            topAppBar = {
                TopAppBar(
                    title = { Text(text = "Test bar") }
                )
            },
            onSaveClicked = { /*TODO*/ },
            onCancelClicked = { /*TODO*/ },
            radioPresentation = RadioPresentation(
                0,
                "test title",
                "test description",
                coverUri
            )
        )
    }
}