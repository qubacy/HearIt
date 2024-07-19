package com.qubacy.hearit.application.ui._common.error

import androidx.annotation.StringRes

data class UIError(
  val id: Long,
  @StringRes val messageResId: Int,
  val isCritical: Boolean
) {

}