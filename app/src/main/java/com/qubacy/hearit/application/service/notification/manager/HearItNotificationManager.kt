package com.qubacy.hearit.application.service.notification.manager

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat

class HearItNotificationManager(context: Context) {
  private var _notificationManager: NotificationManager? = null
  private var _notificationManagerCompat: NotificationManagerCompat? = null

  init {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
      _notificationManager = context.getSystemService(NotificationManager::class.java)
    else
      _notificationManagerCompat = NotificationManagerCompat.from(context)
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun createNotificationChannel(notificationChannel: NotificationChannel) {
    _notificationManager!!.createNotificationChannel(notificationChannel)
  }

  // todo: reconsider this approach:
  @SuppressLint("MissingPermission")
  fun notify(id: Int, notification: Notification) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
      _notificationManager!!.notify(id, notification)
    else
      _notificationManagerCompat!!.notify(id, notification)
  }
}