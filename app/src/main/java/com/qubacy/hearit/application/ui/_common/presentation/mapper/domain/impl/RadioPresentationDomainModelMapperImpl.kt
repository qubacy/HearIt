package com.qubacy.hearit.application.ui._common.presentation.mapper.domain.impl

import com.qubacy.hearit.application._common.uri.parser._common.UriParser
import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui._common.presentation.mapper.domain._common.RadioPresentationDomainModelMapper
import javax.inject.Inject

class RadioPresentationDomainModelMapperImpl @Inject constructor(
  private val _uriParser: UriParser
) : RadioPresentationDomainModelMapper {
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