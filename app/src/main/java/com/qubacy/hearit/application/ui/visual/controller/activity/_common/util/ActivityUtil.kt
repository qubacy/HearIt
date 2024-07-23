package com.qubacy.hearit.application.ui.visual.controller.activity._common.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

fun Context.findActivity(): Activity? {
    var context = this

    while (context is ContextWrapper) {
        if (context is Activity) return context

        context = context.baseContext
    }

    return null
}