package com.qubacy.hearit.application.ui._common.presentation.mapper.media._common

import androidx.media3.common.MediaItem
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation

interface RadioPresentationMediaItemMapper {
  fun map(radioPresentation: RadioPresentation): MediaItem
}