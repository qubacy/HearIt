package com.qubacy.hearit.application.ui._common.presentation.mapper._common

import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation

interface RadioDomainModelRadioPresentationMapper {
  fun map(radioDomainModel: RadioDomainModel): RadioPresentation
}