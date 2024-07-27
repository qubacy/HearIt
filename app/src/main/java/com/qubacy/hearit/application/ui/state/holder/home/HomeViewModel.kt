package com.qubacy.hearit.application.ui.state.holder.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qubacy.hearit.application._common.exception.HearItException
import com.qubacy.hearit.application.domain.usecase.home._common.HomeUseCase
import com.qubacy.hearit.application.ui._common.presentation.mapper._common.RadioDomainModelRadioPresentationMapper
import com.qubacy.hearit.application.ui.state.holder._common.dispatcher._di.ViewModelDispatcherQualifier
import com.qubacy.hearit.application.ui.state.state.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  @ViewModelDispatcherQualifier
  private val _dispatcher: CoroutineDispatcher,
  private val _useCase: HomeUseCase,
  private val _radioMapper: RadioDomainModelRadioPresentationMapper
) : ViewModel() {
  companion object {
    const val TAG = "HomeViewModel"
  }

  private val _state: MutableLiveData<HomeState> = MutableLiveData(HomeState())
  val state: LiveData<HomeState> get() = _state

  private var _getRadioListJob: Job? = null

  fun observeRadioList() {
    if (_getRadioListJob != null) return

    //Log.d(TAG, "observeRadioList();")

    _getRadioListJob = startGettingRadioList()
  }

  fun stopObservingRadioList() {
    if (_getRadioListJob == null) return

    //Log.d(TAG, "stopObservingRadioList();")

    disposeGetRadioListJob()
  }

  private fun disposeGetRadioListJob() {
    _getRadioListJob!!.cancel()
    _getRadioListJob = null
  }

  override fun onCleared() {
    stopObservingRadioList()

    super.onCleared()
  }

  private fun startGettingRadioList(): Job {
    _state.value = _state.value!!.copy(isLoading = true)

    return viewModelScope.launch(_dispatcher) {
      _useCase.getRadioList().map { list ->
        list.map { _radioMapper.map(it) }
      }.onEach {
        // todo: possible bug:
        _state.postValue(_state.value!!.copy(radioList = it, isLoading = false))
      }.catch { cause ->
        if (cause !is HearItException) throw cause

        // todo: possible bug:
        _state.postValue(_state.value!!.copy(error = cause.errorReference, isLoading = false))
      }.collect()
    }
  }
}