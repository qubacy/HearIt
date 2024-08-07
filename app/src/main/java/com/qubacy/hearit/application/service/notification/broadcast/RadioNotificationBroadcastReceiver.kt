package com.qubacy.hearit.application.service.notification.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class RadioNotificationBroadcastReceiver(
  private val _callback: Callback
) : BroadcastReceiver() {
   interface Callback {
    fun onNotificationActionGotten(action: String)
  }

  override fun onReceive(context: Context?, intent: Intent?) {
    if (intent == null) return

    _callback.onNotificationActionGotten(intent.action!!)
  }
}