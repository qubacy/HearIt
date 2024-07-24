package com.qubacy.hearit.application.ui.visual.resource.error

import androidx.annotation.StringRes

data class HearItError(
    val id: Long,
    @StringRes val messageResId: Int,
    val isCritical: Boolean
) {

}