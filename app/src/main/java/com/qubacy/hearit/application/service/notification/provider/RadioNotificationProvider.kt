package com.qubacy.hearit.application.service.notification.provider

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import com.qubacy.hearit.R
import com.qubacy.hearit.application.service.notification._common.RadioNotificationActionEnum

class RadioNotificationProvider(
  private val _context: Context,
) {
  fun createNotification(
    mediaItem: MediaItem,
    channelId: String
  ): Notification {
    val notificationLayout = RemoteViews(
      _context.packageName,
      R.layout.notification_radio_playback
    )

    setupNotificationContent(notificationLayout, mediaItem)
    setupNotificationActions(notificationLayout)

    val notification = NotificationCompat.Builder(_context, channelId)
      .setSmallIcon(R.drawable.ic_launcher_foreground)
      .setCustomContentView(notificationLayout)
      .setStyle(NotificationCompat.DecoratedCustomViewStyle())
      .build()

    return notification
  }

  private fun setupNotificationContent(
    notificationLayout: RemoteViews,
    mediaItem: MediaItem
  ) {
    notificationLayout.setImageViewUri(
      R.id.notification_radio_playback_cover,
      mediaItem.mediaMetadata.artworkUri
    )
    notificationLayout.setTextViewText(
      R.id.notification_radio_playback_title,
      mediaItem.mediaMetadata.title!!
    )
    notificationLayout.setTextViewText(
      R.id.notification_radio_playback_description,
      mediaItem.mediaMetadata.description ?: ""
    )
  }

  private fun setupNotificationActions(notificationLayout: RemoteViews) {
    val prevIntent = Intent(RadioNotificationActionEnum.PREV.action)
    val playPauseIntent = Intent(RadioNotificationActionEnum.PLAY_PAUSE.action)
    val nextIntent = Intent(RadioNotificationActionEnum.NEXT.action)

    notificationLayout.setOnClickPendingIntent(
      R.id.notification_radio_playback_prev_button,
      PendingIntent.getBroadcast(_context, 0, prevIntent, PendingIntent.FLAG_IMMUTABLE)
    )
    notificationLayout.setOnClickPendingIntent(
      R.id.notification_radio_playback_play_pause_button,
      PendingIntent.getBroadcast(_context, 0, playPauseIntent, PendingIntent.FLAG_IMMUTABLE)
    )
    notificationLayout.setOnClickPendingIntent(
      R.id.notification_radio_playback_next_button,
      PendingIntent.getBroadcast(_context, 0, nextIntent, PendingIntent.FLAG_IMMUTABLE)
    )
  }
}