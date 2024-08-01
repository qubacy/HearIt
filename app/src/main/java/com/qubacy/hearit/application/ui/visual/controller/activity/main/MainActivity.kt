package com.qubacy.hearit.application.ui.visual.controller.activity.main

import android.content.ComponentName
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.qubacy.hearit.application.service.RadioPlaybackService
import com.qubacy.hearit.application.ui.visual.controller.activity._common.aspect.CloseableActivity
import com.qubacy.hearit.application.ui.visual.controller.activity._common.aspect.ImagePickerActivity
import com.qubacy.hearit.application.ui.visual.controller.activity._common.aspect.PlayerActivity
import com.qubacy.hearit.application.ui.visual.controller.compose.HearItApp
import com.qubacy.hearit.application.ui.visual.resource.theme.HearItTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(), ImagePickerActivity, PlayerActivity, CloseableActivity {
    private lateinit var _pickImageLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private var _pickImageCallback: ((Uri?) -> Unit)? = null

    private lateinit var _radioPlayer: MediaController

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

    override fun onStart() {
        super.onStart()

        val sessionToken = SessionToken(this,
            ComponentName(this, RadioPlaybackService::class.java)
        )
        val mediaControllerFuture = MediaController.Builder(this, sessionToken).buildAsync()

        mediaControllerFuture.addListener({
            _radioPlayer = mediaControllerFuture.get()
        }, MoreExecutors.directExecutor())
    }

    override fun onStop() {
        _radioPlayer.release()

        super.onStop()
    }

    private fun setupPickImageLauncher() {
        _pickImageLauncher = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            _pickImageCallback?.invoke(uri)
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

    override fun getPlayer(): Player {
        return _radioPlayer
    }
}