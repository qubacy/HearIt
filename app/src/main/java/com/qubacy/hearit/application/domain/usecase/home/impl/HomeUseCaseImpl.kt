package com.qubacy.hearit.application.domain.usecase.home.impl

import com.qubacy.hearit.application.domain._common.model.RadioDomainModel
import com.qubacy.hearit.application.domain.usecase.home._common.HomeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class HomeUseCaseImpl @Inject constructor(
  // todo: provide a repository dependency;
) : HomeUseCase {
  override suspend fun getRadioList(): Flow<List<RadioDomainModel>> {
    // todo: implement:

    return flowOf(
      listOf(
        RadioDomainModel(0, "test title", url = "http://test.url")
      )
    )
  }
}