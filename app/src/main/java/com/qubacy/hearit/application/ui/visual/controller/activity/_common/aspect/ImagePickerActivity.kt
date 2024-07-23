package com.qubacy.hearit.application.ui.visual.controller.activity._common.aspect

import android.net.Uri

interface ImagePickerActivity {
    fun pickImage(callback: (Uri?) -> Unit)
}