package com.qubacy.hearit.application.ui.state.holder.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qubacy.hearit.application.ui.state.state.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
  private val _state: MutableLiveData<HomeState> = MutableLiveData(HomeState.Idle)
  val state: LiveData<HomeState> get() = _state

  fun loadRadioList() {
    TODO("Not yet implemented")
  }
}