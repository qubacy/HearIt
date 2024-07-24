package com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.components.error

import android.content.Context
import com.qubacy.hearit.application.ui.visual.resource.error.HearItError

data class ErrorPresentation(
  val id: Long,
  val message: String,
  val isCritical: Boolean
) {
  companion object {
    fun fromHearItError(error: HearItError, context: Context): ErrorPresentation {
      return ErrorPresentation(
        error.id,
        context.getString(error.messageResId),
        error.isCritical
      )
    }
  }
}