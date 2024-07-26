package com.qubacy.hearit.application.ui._common.presentation.mapper.impl

import com.qubacy.hearit.application._common.uri.parser._common.UriParser
import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui._common.presentation.mapper._common.RadioDomainModelRadioPresentationMapper
import javax.inject.Inject

class RadioDomainModelRadioPresentationMapperImpl @Inject constructor(
  private val _uriParser: UriParser
) : RadioDomainModelRadioPresentationMapper {
  override fun map(radioDomainModel: RadioDomainModel): RadioPresentation {
    return RadioPresentation(
      radioDomainModel.id,
      radioDomainModel.title,
      radioDomainModel.description,
      radioDomainModel.cover?.let { _uriParser.parse(it) },
      radioDomainModel.url
    )
  }
}