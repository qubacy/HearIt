package com.qubacy.hearit.application.service.notification.provider

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import com.qubacy.hearit.R
import com.qubacy.hearit.application.service.notification._common.RadioNotificationActionEnum

class RadioNotificationProvider(
  private val _context: Context,
  private val _channelId: String
) {
  data class State(
    val curMediaItem: MediaItem,
    val isPlaying: Boolean
  )

  private lateinit var _notificationLayout: RemoteViews
  private lateinit var _notificationBuilder: NotificationCompat.Builder

  init {
    initNotificationLayout()
    initNotificationBuilder(_notificationLayout)
  }

  private fun initNotificationLayout() {
    _notificationLayout = RemoteViews(
      _context.packageName,
      R.layout.notification_radio_playback
    )
  }

  private fun initNotificationBuilder(notificationLayout: RemoteViews) {
    setupNotificationActions(notificationLayout)

    _notificationBuilder = NotificationCompat.Builder(_context, _channelId)
      .setSmallIcon(R.drawable.ic_launcher_foreground)
      .setCustomContentView(notificationLayout)
      .setStyle(NotificationCompat.DecoratedCustomViewStyle())
  }

  fun createNotification(
    notificationState: State
  ): Notification {
    setupNotificationContent(_notificationLayout, notificationState)

    return _notificationBuilder.build()
  }

  private fun setupNotificationContent(
    notificationLayout: RemoteViews,
    notificationState: State
  ) {
    val mediaItem = notificationState.curMediaItem

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
    notificationLayout.set(
      R.id.notification_radio_playback_play_pause_button,
      if (notificationState.isPlaying) androidx.media3.session.R.drawable.media3_icon_play
      else androidx.media3.session.R.drawable.media3_icon_pause
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