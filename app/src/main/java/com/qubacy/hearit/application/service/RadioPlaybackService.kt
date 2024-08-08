package com.qubacy.hearit.application.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.qubacy.hearit.application.data.player.model.PlayerInfoDataModel
import com.qubacy.hearit.application.data.player.repository._common.PlayerDataRepository
import com.qubacy.hearit.application.domain.usecase.radio.get._common.GetRadioUseCase
import com.qubacy.hearit.application.service._di.module.RadioPlaybackServiceCoroutineDispatcherQualifier
import com.qubacy.hearit.application.service.media.item.mapper._common.MediaItemRadioDomainModelMapper
import com.qubacy.hearit.application.service.notification._common.RadioNotificationActionEnum
import com.qubacy.hearit.application.service.notification.broadcast.RadioNotificationBroadcastReceiver
import com.qubacy.hearit.application.service.notification.manager.HearItNotificationManager
import com.qubacy.hearit.application.service.notification.provider.RadioNotificationProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RadioPlaybackService : MediaSessionService(), RadioNotificationBroadcastReceiver.Callback {
  companion object {
    const val TAG = "RadioPlaybackService"

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

  private lateinit var _broadcastReceiver: RadioNotificationBroadcastReceiver
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

    setupBroadcastReceiver()
    setupMediaSession()
    observePlayerState(_player!!)
    setupNotification(_notificationManager)
  }

  override fun onDestroy() {
    unregisterReceiver(_broadcastReceiver)

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

  override fun onUpdateNotification(session: MediaSession, startInForegroundRequired: Boolean) {
    super.onUpdateNotification(session, startInForegroundRequired)
  }

  private fun setupBroadcastReceiver() {
    _broadcastReceiver = RadioNotificationBroadcastReceiver(this)

    val intentFilter = IntentFilter().apply {
      RadioNotificationActionEnum.entries.forEach {
        addAction(it.action)
      }
    }

    registerReceiver(_broadcastReceiver, intentFilter)
  }

  private fun setupNotification(notificationManager: HearItNotificationManager) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      _notificationChannel = NotificationChannel(
        NOTIFICATION_CHANNEL_ID, "Radio channel", NotificationManager.IMPORTANCE_DEFAULT
      )

      notificationManager.createNotificationChannel(_notificationChannel)
    }

    val channelId =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) _notificationChannel.id
      else NotificationChannelCompat.DEFAULT_CHANNEL_ID

    _notificationProvider = RadioNotificationProvider(this, channelId)
  }

  private fun showNotification(mediaItem: MediaItem) {
    val notification = _notificationProvider.createNotification(
      RadioNotificationProvider.State(mediaItem, _player!!.isPlaying)
    )

    _notificationManager.notify(1, notification)
  }

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
      Log.d(TAG, "observeCurrentRadioChange(): _getRadioUseCase.getRadio(): radioDomainModel = $radioDomainModel;")

      val mediaItem = _mediaItemRadioDomainModelMapper.map(radioDomainModel)

      postRunnableOnMainThread {
        showNotification(mediaItem)

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

  override fun onNotificationActionGotten(action: String) {
    // todo: reconsider this actions should affect the service directly (seekToPrevious should
    //  be substituted with a cur. media item direct reset, etc.):
    when (action) {
      RadioNotificationActionEnum.PREV.action -> _player!!.seekToPrevious()
      RadioNotificationActionEnum.PLAY_PAUSE.action -> _player!!.apply {
        if (isPlaying) pause() else play()
      }
      RadioNotificationActionEnum.NEXT.action -> _player!!.seekToNext()
    }
  }
}