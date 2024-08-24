package com.qubacy.hearit.application.ui.visual.controller.activity.main.contract.image

import android.content.Context
import android.content.Intent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

class PickImageContract : ActivityResultContracts.PickVisualMedia() {
  override fun createIntent(context: Context, input: PickVisualMediaRequest): Intent {
    return super.createIntent(context, input).apply {
      setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
  }
}