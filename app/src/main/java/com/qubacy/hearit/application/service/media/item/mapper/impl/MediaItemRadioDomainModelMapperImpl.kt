package com.qubacy.hearit.application.service.media.item.mapper.impl

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.qubacy.hearit.application._common.uri.parser._common.UriParser
import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.service.media.item.mapper._common.MediaItemRadioDomainModelMapper
import javax.inject.Inject

class MediaItemRadioDomainModelMapperImpl @Inject constructor(
  private val _uriParser: UriParser
) : MediaItemRadioDomainModelMapper {
  override fun map(radioDomainModel: RadioDomainModel): MediaItem {
    return MediaItem.Builder()
      .setMediaId(radioDomainModel.id.toString())
      .setUri(radioDomainModel.url)
      .setMediaMetadata(
        MediaMetadata.Builder()
          .setTitle(radioDomainModel.title)
          .setDescription(radioDomainModel.description)
          .setArtworkUri(radioDomainModel.cover?.let { _uriParser.parse(it) })
          .build()
      )
      .build()
  }
}