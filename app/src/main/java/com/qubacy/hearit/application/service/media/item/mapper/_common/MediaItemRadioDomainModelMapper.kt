package com.qubacy.hearit.application.service.media.item.mapper._common

import androidx.media3.common.MediaItem
import com.qubacy.hearit.application.domain._common.model.RadioDomainModel

interface MediaItemRadioDomainModelMapper {
  fun map(radioDomainModel: RadioDomainModel): MediaItem
}