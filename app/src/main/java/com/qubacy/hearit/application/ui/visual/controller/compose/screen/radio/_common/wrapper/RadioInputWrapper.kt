package com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio._common.wrapper

import android.net.Uri
import com.qubacy.hearit.application.domain.usecase.radio._common.sketch.RadioDomainSketch

data class RadioInputWrapper(
  val title: String,
  val description: String? = null,
  val coverUri: Uri? = null,
  val url: String
) {
  fun toRadioDomainSketch(): RadioDomainSketch {
    return RadioDomainSketch(title, description, coverUri?.toString(), url)
  }
}