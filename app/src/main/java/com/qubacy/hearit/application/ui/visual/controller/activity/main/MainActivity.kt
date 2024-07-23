package com.qubacy.hearit.application.ui.visual.controller.activity.main

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.qubacy.hearit.application.ui.visual.controller.activity._common.aspect.ImagePickerActivity
import com.qubacy.hearit.application.ui.visual.controller.compose.HearItApp
import com.qubacy.hearit.application.ui.visual.resource.theme.HearItTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(), ImagePickerActivity {
    private lateinit var _pickImageLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private var _pickImageCallback: ((Uri?) -> Unit)? = null

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

    override fun pickImage(callback: (Uri?) -> Unit) {
        _pickImageCallback = callback

        _pickImageLauncher.launch(PickVisualMediaRequest(
            ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}