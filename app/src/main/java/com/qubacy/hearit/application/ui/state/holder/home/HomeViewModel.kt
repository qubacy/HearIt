package com.qubacy.hearit.application.ui.state.holder.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.state.state.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  // todo: injecting a use-case..
) : ViewModel() {
  private val _state: MutableLiveData<HomeState> = MutableLiveData(HomeState.Idle)
  val state: LiveData<HomeState> get() = _state

  // todo: should call a use-case's method directly here:
  private var _loadingRadioListResultFlow: Flow<List<RadioPresentation>> = flowOf()
}