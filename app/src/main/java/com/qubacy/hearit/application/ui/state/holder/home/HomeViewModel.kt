package com.qubacy.hearit.application.ui.state.holder.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qubacy.hearit.application.domain.usecase.home._common.HomeUseCase
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.state.state.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val _useCase: HomeUseCase
) : ViewModel() {
  private val _state: MutableLiveData<HomeState> = MutableLiveData(HomeState.Idle)
  val state: LiveData<HomeState> get() = _state

  // todo: should call a use-case's method directly here:
  private lateinit var _loadingRadioListResultFlow: Flow<List<RadioPresentation>>

  init {
    viewModelScope.launch(Dispatchers.Default + ) {
      _loadingRadioListResultFlow = _useCase.getRadioList().map { list ->
        list.map { RadioPresentation.fromRadioDomainModel(it) }
      }
    }
  }
}