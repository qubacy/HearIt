package com.qubacy.hearit.application.ui._common.presentation

import android.net.Uri
import com.qubacy.hearit.application.domain._common.model.RadioDomainModel

data class RadioPresentation(
  val id: Long,
  val title: String,
  val description: String? = null,
  val cover: Uri? = null,
  val url: String
) {
  companion object {
    fun fromRadioDomainModel(radio: RadioDomainModel): RadioPresentation {
      return RadioPresentation(
        radio.id,
        radio.title,
        radio.description,
        radio.cover?.let { Uri.parse(it) },
        radio.url
      )
    }
  }
}