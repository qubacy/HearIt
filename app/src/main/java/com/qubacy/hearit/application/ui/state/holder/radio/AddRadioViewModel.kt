package com.qubacy.hearit.application.ui.state.holder.radio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.state.state.AddRadioState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRadioViewModel @Inject constructor() : ViewModel() {
  private var _state: MutableLiveData<AddRadioState> = MutableLiveData(AddRadioState.Idle)
  val state: LiveData<AddRadioState> get() = _state

  private var _addedRadio: LiveData<RadioPresentation>? = null

  fun addRadio(radio: RadioPresentation) {
    viewModelScope.launch {
      //_addedRadio =

      // todo: implement..
    }
  }
}