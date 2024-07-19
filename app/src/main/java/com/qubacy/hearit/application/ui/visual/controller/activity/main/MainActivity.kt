package com.qubacy.hearit.application.ui.visual.controller.activity.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.qubacy.hearit.application.ui.visual.controller.compose.HearItApp
import com.qubacy.hearit.application.ui.visual.resource.theme.HearItTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            HearItTheme {
                HearItApp()
            }
        }
    }
}