package com.qubacy.hearit.application.service.notification.provider

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.PendingIntentCompat
import androidx.core.graphics.decodeBitmap
import androidx.media3.common.MediaItem
import com.qubacy.hearit.R
import com.qubacy.hearit.application.service.notification._common.RadioNotificationActionEnum
import com.qubacy.hearit.application.ui.visual.controller.activity.main.MainActivity

class RadioNotificationProvider(
  private val _context: Context,
  private val _channelId: String
) {
  companion object {
    const val TAG = "RadioNotificationProvider"

    const val OPEN_APP_REQUEST_CODE = 0
  }

  data class State(
    val curMediaItem: MediaItem,
    val isPlaying: Boolean,
    val isEnabled: Boolean
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

    val openAppIntent = createOpenAppIntent()

    _notificationBuilder = NotificationCompat.Builder(_context, _channelId)
      .setSmallIcon(R.drawable.notification)
      .setContentIntent(openAppIntent)
      .setCustomContentView(notificationLayout)
      .setStyle(NotificationCompat.DecoratedCustomViewStyle())
  }

  fun createNotification(
    notificationState: State
  ): Notification {
    setupNotificationContent(_notificationLayout, notificationState)

    return _notificationBuilder.build()
  }

  private fun createOpenAppIntent(): PendingIntent {
    val intent = Intent(_context, MainActivity::class.java)

    return PendingIntentCompat.getActivity(
      _context,
      OPEN_APP_REQUEST_CODE,
      intent,
      0,
      false
    )!!
  }

  private fun setupNotificationContent(
    notificationLayout: RemoteViews,
    notificationState: State
  ) {
    Log.d(TAG, "setupNotificationContent(): notificationState = $notificationState;")

    val mediaItem = notificationState.curMediaItem

    mediaItem.mediaMetadata.run {
      Log.d(
        TAG,
        "setupNotificationContent(): title = $title; description = $description; artworkUri = $artworkUri;"
      )
    }

    if (mediaItem.mediaMetadata.artworkUri != null) {
      val loadedCover = loadCover(mediaItem.mediaMetadata.artworkUri!!)

      notificationLayout.setImageViewBitmap(
        R.id.notification_radio_playback_cover,
        loadedCover
      )
    } else {
      notificationLayout.setImageViewResource(
        R.id.notification_radio_playback_cover,
        R.drawable.notification
      )
    }
    notificationLayout.setTextViewText(
      R.id.notification_radio_playback_title,
      mediaItem.mediaMetadata.title!!
    )
    notificationLayout.setTextViewText(
      R.id.notification_radio_playback_description,
      mediaItem.mediaMetadata.description ?: ""
    )
    notificationLayout.setImageViewResource(
      R.id.notification_radio_playback_play_pause_button,
      if (notificationState.isPlaying) androidx.media3.session.R.drawable.media3_icon_pause
      else androidx.media3.session.R.drawable.media3_icon_play
    )
    notificationLayout.setViewVisibility(
      R.id.notification_radio_playback_prev_button,
      if (notificationState.isEnabled) View.VISIBLE else View.GONE
    )
    notificationLayout.setViewVisibility(
      R.id.notification_radio_playback_play_pause_button,
      if (notificationState.isEnabled) View.VISIBLE else View.GONE
    )
    notificationLayout.setViewVisibility(
      R.id.notification_radio_playback_next_button,
      if (notificationState.isEnabled) View.VISIBLE else View.GONE
    )
  }

  // todo: mb it'd be better to form it as a suspend function 'coz of a bitmap loading process?:
  private fun loadCover(coverUri: Uri): Bitmap {
    return when {
      Build.VERSION.SDK_INT < 28 -> {
        MediaStore.Images.Media.getBitmap(_context.contentResolver, coverUri)
      }
      else -> {
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(_context.contentResolver, coverUri))
      }
    }
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