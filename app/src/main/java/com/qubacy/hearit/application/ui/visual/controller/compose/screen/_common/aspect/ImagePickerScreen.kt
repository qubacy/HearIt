package com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.aspect

import android.content.Context
import android.net.Uri
import com.qubacy.hearit.application.ui.visual.controller.activity._common.aspect.ImagePickerActivity
import com.qubacy.hearit.application.ui.visual.controller.activity._common.util.findActivity

object ImagePickerScreen {
    fun pickImage(context: Context, onPicked: (Uri?) -> Unit) {
        val activity = context.findActivity()!!

        if (activity !is ImagePickerActivity) return

        activity.pickImage { uri ->
            onPicked(uri)
        }
    }
}