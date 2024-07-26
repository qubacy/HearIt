package com.qubacy.hearit.application.ui.state.holder.radio.wrapper

import android.net.Uri

data class RadioInputWrapper(
  val title: String,
  val description: String? = null,
  val coverUri: Uri? = null,
  val url: String
) {

}