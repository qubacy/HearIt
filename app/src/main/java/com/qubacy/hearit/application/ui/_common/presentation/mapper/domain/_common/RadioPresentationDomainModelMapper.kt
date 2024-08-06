package com.qubacy.hearit.application.ui._common.presentation.mapper.domain._common

import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation

interface RadioPresentationDomainModelMapper {
  fun map(radioDomainModel: RadioDomainModel): RadioPresentation
}