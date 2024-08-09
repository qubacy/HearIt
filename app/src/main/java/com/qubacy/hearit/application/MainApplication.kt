package com.qubacy.hearit.application

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat
import com.qubacy.hearit.application.service.RadioPlaybackService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
  override fun onCreate() {
    super.onCreate()

    startRadioPlaybackService()
  }

  private fun startRadioPlaybackService() {
    val intent = Intent(this, RadioPlaybackService::class.java)

    ContextCompat.startForegroundService(this, intent)
  }
}