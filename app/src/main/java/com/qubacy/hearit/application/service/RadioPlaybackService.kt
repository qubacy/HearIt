package com.qubacy.hearit.application.service

import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class RadioPlaybackService : MediaSessionService() {
  private var _mediaSession: MediaSession? = null

  override fun onCreate() {
    super.onCreate()

    val radioPlayer = ExoPlayer.Builder(this).build()

    _mediaSession = MediaSession.Builder(this, radioPlayer).build()
  }

  override fun onDestroy() {
    _mediaSession?.run {
      player.release()
      release()

      _mediaSession = null
    }

    super.onDestroy()
  }

  override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
    return _mediaSession
  }
}