package com.qubacy.hearit.application.ui._common.presentation

import android.net.Uri

data class RadioPresentation(
  val id: Long,
  val title: String,
  val description: String? = null,
  val cover: Uri? = null,
  val url: String
) {

}