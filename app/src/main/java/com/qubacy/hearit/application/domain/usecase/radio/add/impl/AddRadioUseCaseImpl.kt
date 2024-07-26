package com.qubacy.hearit.application.domain.usecase.radio.add.impl

import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.RadioDomainSketch
import com.qubacy.hearit.application.domain.usecase.radio.add._common.AddRadioUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddRadioUseCaseImpl @Inject constructor(
  // todo: provide a repository;
) : AddRadioUseCase {
  override suspend fun addRadio(radio: RadioDomainSketch): Flow<RadioDomainModel> {
    // todo: implement:

    return flow {  }
  }
}