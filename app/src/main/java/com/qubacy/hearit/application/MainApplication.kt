package com.qubacy.hearit.application

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.qubacy.hearit.application.service.RadioPlaybackService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
  companion object {
    const val TAG = "MainApplication"
  }

  override fun onCreate() {
    super.onCreate()

    startRadioPlaybackService()
  }

  override fun onTerminate() {
    super.onTerminate()

    Log.d(TAG, "onTerminate()")
  }

  private fun startRadioPlaybackService() {
    val intent = Intent(this, RadioPlaybackService::class.java)

    ContextCompat.startForegroundService(this, intent)
  }
}