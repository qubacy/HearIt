package com.qubacy.hearit.application.service

import android.os.Handler
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.qubacy.hearit.application.data.player.model.PlayerInfoDataModel
import com.qubacy.hearit.application.data.player.repository._common.PlayerDataRepository
import com.qubacy.hearit.application.domain.usecase.radio.get._common.GetRadioUseCase
import com.qubacy.hearit.application.service._di.module.RadioPlaybackServiceCoroutineDispatcherQualifier
import com.qubacy.hearit.application.service.media.item.mapper._common.MediaItemRadioDomainModelMapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RadioPlaybackService : MediaSessionService() {
  @Inject
  lateinit var _playerRepository: PlayerDataRepository
  @Inject
  lateinit var _getRadioUseCase: GetRadioUseCase
  @Inject
  lateinit var _mediaItemRadioDomainModelMapper: MediaItemRadioDomainModelMapper

  @Inject
  @RadioPlaybackServiceCoroutineDispatcherQualifier
  lateinit var _coroutineDispatcher: CoroutineDispatcher

  private var _coroutineScope: CoroutineScope? = null

  private var _player: ExoPlayer? = null
  private var _mediaSession: MediaSession? = null

  override fun onCreate() {
    super.onCreate()

    _coroutineScope = CoroutineScope(_coroutineDispatcher)

    setupMediaSession()
    observePlayerState(_player!!)
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

  override fun onUpdateNotification(session: MediaSession, startInForegroundRequired: Boolean) {
    super.onUpdateNotification(session, startInForegroundRequired)
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
      val mediaItem = _mediaItemRadioDomainModelMapper.map(radioDomainModel)

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