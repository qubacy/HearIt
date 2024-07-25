package com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio._common.wrapper

import android.net.Uri

data class RadioInputWrapper(
  val title: String,
  val description: String? = null,
  val coverUri: Uri? = null,
  val url: String
) {

}