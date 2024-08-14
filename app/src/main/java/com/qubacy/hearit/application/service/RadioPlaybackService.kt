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
import android.util.Log
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.ServiceCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.qubacy.hearit.R
import com.qubacy.hearit.application._common.player.bus._common.PlayerStatePacketBus
import com.qubacy.hearit.application._common.player.packet.PlayerStatePacket
import com.qubacy.hearit.application._common.player.packet.PlayerStatePacketBody
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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

  private var _radioList: List<MediaItem> = listOf()
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
    setupNotification(_notificationManager)
    setupPlayerStatePacketBus()
    observeRadioList()

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
    _coroutineScope!!.launch {
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

      setPlayingState(playerStatePacketBody.isPlaying)
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

  private fun observeRadioList() {
    _coroutineScope!!.launch {
      getRadioUseCase.getRadioList().map { list ->
        list.map { mediaItemRadioDomainModelMapper.map(it) }
      }.onEach {
        _radioList = it
      }.collect()
    }
  }

  private fun changePlayingState() {
    applyPlayingState(!_isPlaying)
    broadcastPlayerState(PlayerStatePacketBody(
      radioId = _curMediaItem.mediaId.toLong(),
      isPlaying = _isPlaying
    ))
  }

  private fun setPlayingState(isPlaying: Boolean) {
    applyPlayingState(isPlaying)
  }

  private fun applyPlayingState(isPlaying: Boolean) {
    _isPlaying = isPlaying

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

  private fun postRunnableOnMainThread(runnable: () -> Unit) {
    Handler(mainLooper).post(runnable)
  }

  override fun onNotificationActionGotten(action: String) {
    when (action) {
      RadioNotificationActionEnum.PREV.action -> seekToPrevRadio()
      RadioNotificationActionEnum.PLAY_PAUSE.action -> changePlayingState()
      RadioNotificationActionEnum.NEXT.action -> seekToNextRadio()
    }
  }

  private fun seekToPrevRadio() {
    seekToNeighborRadio(true)
  }

  private fun seekToNextRadio() {
    seekToNeighborRadio(false)
  }

  private fun seekToNeighborRadio(toPrev: Boolean) {
    val curRadioIndex = _radioList.indexOf(_curMediaItem)
    val prevRadioIndex =
      if (toPrev) (curRadioIndex - 1).let { if (it < 0) _radioList.size - 1 else it }
      else (curRadioIndex + 1).let { if (it >= _radioList.size) 0 else it }

    changeCurRadio(_radioList[prevRadioIndex])

    broadcastPlayerState(PlayerStatePacketBody(
      radioId = _radioList[prevRadioIndex].mediaId.toLong(),
      isPlaying = _isPlaying
    ))
  }

  private fun broadcastPlayerState(playerStatePacketBody: PlayerStatePacketBody) {
    _coroutineScope!!.launch {
      playerStatePacketBus.postPlayerStatePacket(
        PlayerStatePacket(playerStatePacketBody, hashCode().toString())
      )
    }
  }
}