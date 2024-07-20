package com.qubacy.hearit.application.ui.state.holder.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.state.state.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
  private val _state: MutableLiveData<HomeState> = MutableLiveData(HomeState.Idle)
  val state: LiveData<HomeState> get() = _state

  private var _loadingRadioList: LiveData<List<RadioPresentation>>? = null

  fun loadRadioList() {
    if (_loadingRadioList != null) return

    viewModelScope.launch {
      //_loadingRadioList = ;

    }
  }
}