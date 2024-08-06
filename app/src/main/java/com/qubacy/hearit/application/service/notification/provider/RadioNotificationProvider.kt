package com.qubacy.hearit.application.service.notification.provider

import android.content.Context
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaNotification
import androidx.media3.session.MediaSession
import com.google.common.collect.ImmutableList

@UnstableApi
class RadioNotificationProvider(
  private val _context: Context
) : MediaNotification.Provider {
  companion object {
    const val NOTIFICATION_ID = 1
    const val CHANNEL_ID = "radioChannel"
  }

  override fun createNotification(
    mediaSession: MediaSession,
    customLayout: ImmutableList<CommandButton>,
    actionFactory: MediaNotification.ActionFactory,
    onNotificationChangedCallback: MediaNotification.Provider.Callback,
  ): MediaNotification {
    val notification = NotificationCompat.Builder(_context, CHANNEL_ID)
      .build()

    return MediaNotification(NOTIFICATION_ID, notification)
  }

  override fun handleCustomCommand(
    session: MediaSession,
    action: String,
    extras: Bundle
  ): Boolean {
    TODO("Not yet implemented")
  }
}