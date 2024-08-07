package com.qubacy.hearit.application.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Handler
import androidx.annotation.OptIn
import androidx.core.app.NotificationChannelCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.qubacy.hearit.application.data.player.model.PlayerInfoDataModel
import com.qubacy.hearit.application.data.player.repository._common.PlayerDataRepository
import com.qubacy.hearit.application.domain.usecase.radio.get._common.GetRadioUseCase
import com.qubacy.hearit.application.service._di.module.RadioPlaybackServiceCoroutineDispatcherQualifier
import com.qubacy.hearit.application.service.media.item.mapper._common.MediaItemRadioDomainModelMapper
import com.qubacy.hearit.application.service.notification.manager.HearItNotificationManager
import com.qubacy.hearit.application.service.notification.provider.RadioNotificationProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RadioPlaybackService : MediaSessionService() {
  companion object {
    const val NOTIFICATION_CHANNEL_ID = "radioChannel"
  }

  @Inject
  lateinit var _playerRepository: PlayerDataRepository
  @Inject
  lateinit var _getRadioUseCase: GetRadioUseCase
  @Inject
  lateinit var _mediaItemRadioDomainModelMapper: MediaItemRadioDomainModelMapper

  @Inject
  @RadioPlaybackServiceCoroutineDispatcherQualifier
  lateinit var _coroutineDispatcher: CoroutineDispatcher

  private lateinit var _notificationManager: HearItNotificationManager
  private lateinit var _notificationChannel: NotificationChannel

  private var _coroutineScope: CoroutineScope? = null

  private var _player: ExoPlayer? = null
  private var _mediaSession: MediaSession? = null
  private lateinit var _notificationProvider: RadioNotificationProvider

  override fun onCreate() {
    super.onCreate()

    _notificationManager = HearItNotificationManager(this)
    _coroutineScope = CoroutineScope(_coroutineDispatcher)

    setupMediaSession()
    observePlayerState(_player!!)
    setupNotification(_notificationManager)
  }

  override fun onDestroy() {
    _mediaSession?.run {
      player.release()
      release()

      _mediaSession = null
    }
    _coroutineScope?.cancel()

    super.onDestroy()
  }

  override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
    return _mediaSession
  }

  @OptIn(UnstableApi::class)
  override fun onUpdateNotification(session: MediaSession, startInForegroundRequired: Boolean) {
    super.onUpdateNotification(session, startInForegroundRequired)
  }

  @OptIn(UnstableApi::class)
  private fun setupNotification(notificationManager: HearItNotificationManager) {
    //setMediaNotificationProvider(RadioNotificationProvider(this))
    _notificationProvider = RadioNotificationProvider(this)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      _notificationChannel = NotificationChannel(
        NOTIFICATION_CHANNEL_ID, "Radio channel", NotificationManager.IMPORTANCE_DEFAULT
      )

      notificationManager.createNotificationChannel(_notificationChannel)
    }
  }

  private fun showNotification(mediaItem: MediaItem) {
    val channelId =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) _notificationChannel.id
      else NotificationChannelCompat.DEFAULT_CHANNEL_ID
    val notification = _notificationProvider.createNotification(mediaItem, channelId)

    _notificationManager.notify(1, notification)
  }

  @OptIn(UnstableApi::class)
  private fun setupMediaSession() {
    _player = ExoPlayer.Builder(this).build()
    _mediaSession = MediaSession.Builder(this, _player!!).build()
  }

  private fun observePlayerState(player: Player) {
    _coroutineScope!!.launch {
      _playerRepository.getPlayerInfo().collect {
        processPlayerState(it)
      }
    }

    player.addListener(object : Player.Listener {
      override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)

        // todo: implement..


      }
    })
  }

  private suspend fun processPlayerState(playerState: PlayerInfoDataModel) {
    playerState.curRadioId?.let { observeCurrentRadioChange(it) }

    // todo: it's already handled by the Player:
//    postRunnableOnMainThread {
//      if (playerState.isPlaying) _player?.play() else _player?.stop()
//    }
  }

  private suspend fun observeCurrentRadioChange(radioId: Long) {
    _getRadioUseCase.getRadio(radioId).collect { radioDomainModel ->
      val mediaItem = _mediaItemRadioDomainModelMapper.map(radioDomainModel)

      showNotification(mediaItem)

      postRunnableOnMainThread {
        // todo: is it a good idea to reset the media item?:
        _player?.apply {
          setMediaItem(mediaItem)
          prepare()
        }
      }
    }
  }

  private fun postRunnableOnMainThread(runnable: () -> Unit) {
    Handler(mainLooper).post(runnable)
  }
}