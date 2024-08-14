package com.qubacy.hearit.application.ui.visual.controller.activity.main

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.qubacy.hearit.application._common.player.bus._common.PlayerStatePacketBus
import com.qubacy.hearit.application._common.player.packet.PlayerStatePacket
import com.qubacy.hearit.application._common.player.packet.PlayerStatePacketBody
import com.qubacy.hearit.application.ui.visual.controller.activity._common.aspect.CloseableActivity
import com.qubacy.hearit.application.ui.visual.controller.activity._common.aspect.ImagePickerActivity
import com.qubacy.hearit.application.ui.visual.controller.activity._common.aspect.PlayerActivity
import com.qubacy.hearit.application.ui.visual.controller.compose.HearItApp
import com.qubacy.hearit.application.ui.visual.resource.theme.HearItTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), ImagePickerActivity, PlayerActivity, CloseableActivity {
    companion object {
        const val TAG = "MainActivity"
    }

    @Inject
    lateinit var playerStatePacketBus: PlayerStatePacketBus

    private lateinit var _pickImageLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private var _pickImageCallback: ((Uri?) -> Unit)? = null

    private var _playerCallback: PlayerActivity.Callback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupPickImageLauncher()

        enableEdgeToEdge()
        setContent {
            HearItTheme {
                HearItApp()
            }
        }
    }

    private fun setupPickImageLauncher() {
        _pickImageLauncher = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            _pickImageCallback?.invoke(uri)
        }
    }

    private fun setupPlayerStatePacketBus() {
        lifecycleScope.launch {
            playerStatePacketBus.playerStatePacket.collect {
                if (it.senderId == this@MainActivity.hashCode().toString()) return@collect

                _playerCallback!!.onPlayerStatePacketGotten(it.body)
            }
        }
    }

    override fun pickImage(callback: (Uri?) -> Unit) {
        _pickImageCallback = callback

        _pickImageLauncher.launch(PickVisualMediaRequest(
            ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    override fun close() {
        finishAndRemoveTask()
    }

    override fun setPlayerActivityCallback(callback: PlayerActivity.Callback) {
        _playerCallback = callback

        setupPlayerStatePacketBus()
    }

    override fun setPlayerState(playerStatePacketBody: PlayerStatePacketBody) {
        Log.d(TAG, "setPlayerState(): playerStatePacketBody = $playerStatePacketBody;")

        lifecycleScope.launch {
            playerStatePacketBus.postPlayerStatePacket(
                PlayerStatePacket(playerStatePacketBody, hashCode().toString())
            )
        }
    }
}