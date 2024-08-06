package com.qubacy.hearit.application.ui._common.presentation.mapper.media.impl

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui._common.presentation.mapper.media._common.RadioPresentationMediaItemMapper
import javax.inject.Inject

class RadioPresentationMediaItemMapperImpl @Inject constructor(

) : RadioPresentationMediaItemMapper {
  override fun map(radioPresentation: RadioPresentation): MediaItem {
    return MediaItem.Builder()
      .setUri(radioPresentation.url)
      .setMediaMetadata(
        MediaMetadata.Builder()
          .setTitle(radioPresentation.title)
          .setSubtitle(radioPresentation.description)
          .setArtworkUri(radioPresentation.cover)
          .build()
      )
      .build()
  }
}