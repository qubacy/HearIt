package com.qubacy.hearit.application.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.ServiceCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.qubacy.hearit.R
import com.qubacy.hearit.application._common.player.bus._common.PlayerStatePacketBus
import com.qubacy.hearit.application._common.player.packet.PlayerStatePacketBody
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RadioPlaybackService : Service(), RadioNotificationBroadcastReceiver.Callback {
  companion object {
    const val TAG = "RadioPlaybackService"

    const val NOTIFICATION_CHANNEL_ID = "radioChannel"
    const val NOTIFICATION_ID = 100
  }

  @Inject
  lateinit var playerStatePacketBus: PlayerStatePacketBus

  @Inject
  lateinit var playerRepository: PlayerDataRepository
  @Inject
  lateinit var getRadioUseCase: GetRadioUseCase
  @Inject
  lateinit var mediaItemRadioDomainModelMapper: MediaItemRadioDomainModelMapper

  @Inject
  @RadioPlaybackServiceCoroutineDispatcherQualifier
  lateinit var coroutineDispatcher: CoroutineDispatcher

  private lateinit var _broadcastReceiver: RadioNotificationBroadcastReceiver
  private lateinit var _notificationManager: HearItNotificationManager
  private lateinit var _notificationChannel: NotificationChannel

  private var _coroutineScope: CoroutineScope? = null

  private var _player: ExoPlayer? = null
  private var _mediaSession: MediaSession? = null
  private lateinit var _notificationProvider: RadioNotificationProvider

  private lateinit var _curMediaItem: MediaItem
  private var _isPlaying: Boolean = false
  private var _isEnabled: Boolean = false

  private var _getCurRadioJob: Job? = null

  override fun onCreate() {
    super.onCreate()

    _notificationManager = HearItNotificationManager(this)
    _coroutineScope = CoroutineScope(coroutineDispatcher)

    setupBroadcastReceiver()
    setupMediaSession()
    //observePlayerState(_player!!)
    setupNotification(_notificationManager)
    setupPlayerStatePacketBus()

    setupForegroundService()
  }

  override fun onDestroy() {
    unregisterReceiver(_broadcastReceiver)

    _mediaSession?.run {
      player.release()
      release()

      _mediaSession = null
    }

    disposeGetCurRadioJob()

    _coroutineScope?.cancel()
    _coroutineScope = null

    super.onDestroy()
  }

  override fun onBind(intent: Intent?): IBinder? {
    return null
  }

  private fun setupForegroundService() {
    _curMediaItem = MediaItem.Builder().setMediaMetadata(
      MediaMetadata.Builder().setTitle(getString(R.string.radio_notification_init_title)).build()
    ).build()

    val initNotification = _notificationProvider.createNotification(
      RadioNotificationProvider.State(_curMediaItem, _isPlaying, _isEnabled)
    )

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
      startForeground(NOTIFICATION_ID, initNotification)
    } else {
      ServiceCompat.startForeground(
        this,
        NOTIFICATION_ID,
        initNotification,
        ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
      )
    }
  }

  private fun setupPlayerStatePacketBus() {
    _coroutineScope!!.launch(coroutineDispatcher) {
      playerStatePacketBus.playerStatePacket.collect {
        if (it.senderId == hashCode().toString()) return@collect

        processPlayerStatePacketBody(it.body)
      }
    }
  }

  private fun disposeGetCurRadioJob() {
    _getCurRadioJob?.cancel()
    _getCurRadioJob = null
  }

  private fun processPlayerStatePacketBody(playerStatePacketBody: PlayerStatePacketBody) {
    playerStatePacketBody.let {
      if (it.radioId != null) observeCurRadio(it.radioId)

      changePlayingState(playerStatePacketBody.isPlaying)
    }
  }

  private fun observeCurRadio(radioId: Long) {
    if (_getCurRadioJob != null) disposeGetCurRadioJob()

    _getCurRadioJob = _coroutineScope!!.launch {
      getRadioUseCase.getRadio(radioId).collect {
        changeCurRadio(mediaItemRadioDomainModelMapper.map(it))
      }
    }
  }

  private fun changePlayingState(isPlaying: Boolean? = null) {
    _isPlaying = isPlaying ?: !_isPlaying

    postRunnableOnMainThread {
      _player!!.apply {
        if (_isPlaying) play() else pause()
      }
    }

    showNotification()
  }

  private fun changeCurRadio(mediaItem: MediaItem) {
    changePlayerMediaItem(mediaItem)
    showNotification()
  }

  private fun changePlayerMediaItem(mediaItem: MediaItem) {
    _curMediaItem = mediaItem

    if (!_isEnabled) _isEnabled = true

    postRunnableOnMainThread {
      _player!!.apply {
        setMediaItem(_curMediaItem)
        prepare()
      }
    }
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

  private fun showNotification() {
    postRunnableOnMainThread {
      val notification = _notificationProvider.createNotification(
        RadioNotificationProvider.State(_curMediaItem, _isPlaying, _isEnabled)
      )

      _notificationManager.notify(NOTIFICATION_ID, notification)
    }
  }

  private fun setupMediaSession() {
    _player = ExoPlayer.Builder(this).build()
    _mediaSession = MediaSession.Builder(this, _player!!).build()
  }

//  private fun observePlayerState(player: Player) {
//    _coroutineScope!!.launch {
//      _playerRepository.getPlayerInfo().collect {
//        processPlayerState(it)
//      }
//    }
//
//    player.addListener(object : Player.Listener {
//      override fun onIsPlayingChanged(isPlaying: Boolean) {
//        super.onIsPlayingChanged(isPlaying)
//
//        // todo: implement..
//
//
//      }
//    })
//  }

//  private suspend fun processPlayerState(playerState: PlayerInfoDataModel) {
//    playerState.curRadioId?.let { observeCurrentRadioChange(it) }
//
//    // todo: it's already handled by the Player:
////    postRunnableOnMainThread {
////      if (playerState.isPlaying) _player?.play() else _player?.stop()
////    }
//  }

//  private suspend fun observeCurrentRadioChange(radioId: Long) {
//    getRadioUseCase.getRadio(radioId).collect { radioDomainModel ->
//      Log.d(TAG, "observeCurrentRadioChange(): _getRadioUseCase.getRadio(): radioDomainModel = $radioDomainModel;")
//
//      val mediaItem = mediaItemRadioDomainModelMapper.map(radioDomainModel)
//
//      postRunnableOnMainThread {
//        showNotification(mediaItem)
//
//        // todo: is it a good idea to reset the media item?:
//        changePlayerMediaItem(mediaItem)
//      }
//    }
//  }

  private fun postRunnableOnMainThread(runnable: () -> Unit) {
    Handler(mainLooper).post(runnable)
  }

  override fun onNotificationActionGotten(action: String) {
    // todo: reconsider this actions should affect the service directly (seekToPrevious should
    //  be substituted with a cur. media item direct reset, etc.):
    when (action) {
      RadioNotificationActionEnum.PREV.action -> seekToPrevRadio()
      RadioNotificationActionEnum.PLAY_PAUSE.action -> changePlayingState()
      RadioNotificationActionEnum.NEXT.action -> seekToNextRadio()
    }
  }

  private fun seekToPrevRadio() {
    // todo: implement (should change the cur. radio to the prev. one..;


  }

  private fun seekToNextRadio() {
    // todo: implement (should change the cur. radio to the next. one..;


  }
}