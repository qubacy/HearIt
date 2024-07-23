package com.qubacy.hearit.application.ui.visual.controller._common.navigation.util

import androidx.navigation.NavController

fun NavController.setResult(key: String, value: Any) {
    previousBackStackEntry?.savedStateHandle?.set(key, value)
}

fun <T> NavController.consumeResult(key: String): T? {
    val savedStateHandle = currentBackStackEntry?.savedStateHandle
    val value = savedStateHandle?.get<T>(key)

    savedStateHandle?.set(key, null)

    return value
}