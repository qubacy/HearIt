package com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio._common

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.qubacy.hearit.R
import com.qubacy.hearit.application._common.error.ErrorReference
import com.qubacy.hearit.application._common.resources.util.getUriFromResource
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.state.holder.radio.wrapper.RadioInputWrapper
import com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.components.error.provider.ErrorWidgetProvider
import com.qubacy.hearit.application.ui.visual.resource.theme.HearItTheme
import kotlinx.coroutines.CoroutineScope

data class RadioScreenTopAppBarData(
    val title: String,
    val isLoading: Boolean,
    val onBackPressed: () -> Unit
)

@Composable
fun RadioScreenContent(
    onPickImageClicked: ((Uri?) -> Unit) -> Unit,
    onSaveClicked: (RadioInputWrapper) -> Unit,
    onCancelClicked: () -> Unit,
    onErrorDismissed: () -> Unit,

    errorWidgetProvider: ErrorWidgetProvider,

    modifier: Modifier = Modifier,

    topAppBarData: RadioScreenTopAppBarData? = null,
    isLoading: Boolean = false,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    snackbarCoroutineScope: CoroutineScope = rememberCoroutineScope(),
    radioPresentation: RadioPresentation? = null,
    error: ErrorReference? = null
) {
    Scaffold(
        topBar = { topAppBarData?.let { RadioScreenTopAppBar(it) } },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        val normalGap = dimensionResource(id = R.dimen.gap_normal)

        ConstraintLayout(
            modifier = modifier.then(
                Modifier
                    .padding(paddingValues)
                    .padding(normalGap)
                    .fillMaxSize())
        ) {
            var title by remember(key1 = radioPresentation?.title) {
                mutableStateOf(radioPresentation?.title ?: "")
            }
            var description by remember(key1 = radioPresentation?.description) {
                mutableStateOf(radioPresentation?.description)
            }
            var url by remember(key1 = radioPresentation?.url) {
                mutableStateOf(radioPresentation?.url ?: "")
            }
            var imageUri: Uri? by remember(key1 = radioPresentation?.cover) {
                mutableStateOf(radioPresentation?.cover)
            }

            val titleInputDescription = stringResource(
                id = R.string.radio_screen_content_title_input_description
            )
            val descriptionInputDescription = stringResource(
                id = R.string.radio_screen_content_description_input_description
            )
            val urlInputDescription = stringResource(
                id = R.string.radio_screen_content_url_input_description
            )

            val (
                titleRef,
                descriptionRef,
                urlRef,
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
                modifier = Modifier
                    .constrainAs(titleRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)

                        width = Dimension.matchParent
                    }
                    .semantics {
                        contentDescription = titleInputDescription
                    },
                enabled = !isLoading
            )

            RadioScreenTextInput(
                value = description ?: "",
                onValueChanged = { description = it },
                R.drawable.paragraph,
                R.string.radio_screen_content_description_input_icon_description,
                R.string.radio_screen_content_description_input_label,
                modifier = Modifier
                    .constrainAs(descriptionRef) {
                        top.linkTo(titleRef.bottom)
                        start.linkTo(parent.start)

                        width = Dimension.matchParent
                    }
                    .semantics {
                        contentDescription = descriptionInputDescription
                    },
                enabled = !isLoading
            )

            RadioScreenTextInput(
                value = url,
                onValueChanged = { url = it },
                R.drawable.link,
                R.string.radio_screen_content_url_input_icon_description,
                R.string.radio_screen_content_url_input_label,
                modifier = Modifier
                    .constrainAs(urlRef) {
                        top.linkTo(descriptionRef.bottom)
                        start.linkTo(parent.start)

                        width = Dimension.matchParent
                    }
                    .semantics {
                        contentDescription = urlInputDescription
                    },
                enabled = !isLoading
            )

            val normalIconPadding = dimensionResource(id = R.dimen.icon_padding_normal)

            RadioScreenIcon(
                drawableRes = R.drawable.image,
                contentDescriptionRes = R.string.radio_screen_content_image_input_icon_description,
                modifier = Modifier
                    .constrainAs(imageIconRef) {
                        top.linkTo(urlRef.bottom, normalGap)
                        start.linkTo(parent.start)
                    }
                    .padding(
                        start = normalIconPadding,
                        top = normalIconPadding,
                        end = normalIconPadding
                    )
                    .size(dimensionResource(id = R.dimen.icon_size_normal))
            )

            Text(
                text = stringResource(id = R.string.radio_screen_content_image_input_hint),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.constrainAs(imageHintRef) {
                    top.linkTo(imageIconRef.top)
                    start.linkTo(imageIconRef.end)
                    end.linkTo(imageButtonRef.start)

                    width = Dimension.fillToConstraints
                }
            )

            RadioScreenImageButton(
                onClick = {
                    onPickImageClicked { uri ->
                        imageUri = uri
                    }
                },
                modifier = Modifier
                    .constrainAs(imageButtonRef) {
                        top.linkTo(imageIconRef.top)
                        start.linkTo(imageHintRef.end)
                        end.linkTo(parent.end)
                    },
                enabled = !isLoading
            )

            RadioScreenImagePreview(
                imageUri = imageUri,
                onCloseClicked = {
                    imageUri = null
                },
                modifier = Modifier.constrainAs(imagePreviewRef) {
                    top.linkTo(imageHintRef.bottom, normalGap)
                    start.linkTo(parent.start)
                    bottom.linkTo(saveButtonRef.top, normalGap)

                    width = Dimension.matchParent
                    height = Dimension.fillToConstraints
                },
                enabled = !isLoading
            )

            Button(
                enabled = radioPresentation?.let {
                    it.title != title || it.description != description
                    || it.cover != imageUri || it.url != url
                } ?: true && !isLoading,
                onClick = {
                    onSaveClicked(RadioInputWrapper(title, description, imageUri, url))
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
                enabled = !isLoading,
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

        error?.let {
            errorWidgetProvider.ErrorWidget(
                error = it,
                snackbarHostState = snackbarHostState,
                coroutineScope = snackbarCoroutineScope,
                onNotCriticalDismissRequest = onErrorDismissed
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RadioScreenTopAppBar(
    topAppBarData: RadioScreenTopAppBarData
) {
    val topAppBarDescription = stringResource(
        id = R.string.radio_screen_content_top_app_bar_description
    )

    TopAppBar(
        title = {
            Text(
                text = topAppBarData.title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            IconButton(
                onClick = topAppBarData.onBackPressed
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(
                        id = R.string.radio_screen_content_top_app_bar_back_button_description
                    )
                )
            }
        },
        modifier = Modifier.semantics {
            contentDescription = topAppBarDescription
        }
    )
}

@Composable
fun RadioScreenTextInput(
    value: String,
    onValueChanged: (String) -> Unit,
    @DrawableRes iconDrawableRes: Int,
    @StringRes iconContentDescriptionRes: Int,
    @StringRes labelRes: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val iconSize = dimensionResource(id = R.dimen.icon_size_normal)

    OutlinedTextField(
        value = value,
        enabled = enabled,
        onValueChange = onValueChanged,
        leadingIcon = {
            RadioScreenIcon(
                drawableRes = iconDrawableRes,
                contentDescriptionRes = iconContentDescriptionRes,
                modifier = Modifier.size(iconSize)
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
    Icon(
        painter = painterResource(id = drawableRes),
        contentDescription = stringResource(id = contentDescriptionRes),
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
    )
}

@Composable
fun RadioScreenImageButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val iconButtonDescription = stringResource(
        id = R.string.radio_screen_content_image_input_choose_button_description
    )

    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.then(Modifier.semantics {
            contentDescription = iconButtonDescription
        })
    ) {
        RadioScreenIcon(
            drawableRes = R.drawable.more,
            contentDescriptionRes = R.string.radio_screen_content_image_input_choose_button_icon_description
        )
    }
}

@Composable
fun RadioScreenImagePreview(
    imageUri: Uri?,
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val imageButtonDescription = stringResource(
        id = R.string.radio_screen_content_image_input_preview_close_button_description
    )

    AnimatedVisibility(
        visible = imageUri != null,
        modifier = modifier.then(
            Modifier.clipToBounds()
        )
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
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
                    modifier = Modifier.clip(AbsoluteRoundedCornerShape(12.dp))
                )
            }

            val buttonMargin = dimensionResource(id = R.dimen.gap_small_less)

            FilledTonalIconButton(
                onClick = onCloseClicked,
                enabled = enabled,
                modifier = Modifier
                    .semantics {
                        contentDescription = imageButtonDescription
                    }
                    .align(Alignment.TopEnd)
                    .offset(-buttonMargin, buttonMargin)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = ""
                )
            }
        }
    }
}

@Preview
@Composable
fun RadioScreenContent() {
    val resources = LocalContext.current.resources
    val resourceId = R.drawable.space
    val coverUri = resources.getUriFromResource(resourceId)

    val errorWidgetProvider = ErrorWidgetProvider()

    HearItTheme {
        RadioScreenContent(
            onPickImageClicked = {  },
            onSaveClicked = {  },
            onCancelClicked = {  },
            onErrorDismissed = {  },
            errorWidgetProvider = errorWidgetProvider,
            topAppBarData = RadioScreenTopAppBarData("Test bar", false, {}),
            radioPresentation = RadioPresentation(
                0,
                "test title",
                "test description",
                coverUri,
                "http://url.com/radio"
            )
        )
    }
}