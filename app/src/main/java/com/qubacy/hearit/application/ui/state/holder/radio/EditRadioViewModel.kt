package com.qubacy.hearit.application.ui.state.holder.radio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation
import com.qubacy.hearit.application.ui.state.state.EditRadioState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRadioViewModel @Inject constructor(
  private val _savedStateHandle: SavedStateHandle
) : ViewModel() {
  companion object {
    const val RADIO_ID_KEY_NAME = "radioId"
  }

  private val _radioId: Long = _savedStateHandle[RADIO_ID_KEY_NAME]!!

  private var _state: MutableLiveData<EditRadioState> = MutableLiveData(EditRadioState.Idle)
  val state: LiveData<EditRadioState> get() = _state

  //private var _loadingRadio: LiveData<RadioPresentation> // todo: calling a repo's method..
  private var _savedRadio: LiveData<RadioPresentation>? = null

  /**
   * Meant to be called in Loaded state;
   */
  fun saveRadio(radio: RadioPresentation) {
    viewModelScope.launch {
      //_savedRadio =

      // todo: implement..
    }
  }
}