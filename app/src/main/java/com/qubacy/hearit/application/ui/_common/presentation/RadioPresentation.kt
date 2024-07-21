package com.qubacy.hearit.application.ui._common.presentation

import android.net.Uri

data class RadioPresentation(
  val id: Long = UNSPECIFIED_ID,
  val title: String,
  val description: String? = null,
  val cover: Uri? = null,
) {
  companion object {
    const val UNSPECIFIED_ID = -1L
  }
}