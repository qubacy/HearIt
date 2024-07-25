package com.qubacy.hearit.application.ui.state.holder.radio.validator.impl

import com.qubacy.hearit.application.ui.state.holder.radio.validator._common.RadioInputWrapperValidator
import com.qubacy.hearit.application.ui.visual.controller.compose.screen.radio._common.wrapper.RadioInputWrapper

class RadioInputWrapperValidatorImpl(
  private val _urlValidator: (String) -> Boolean
) : RadioInputWrapperValidator {
  override fun validate(data: RadioInputWrapper): Boolean {
    val isDescriptionValid = data.description?.let { data.description.isNotEmpty() } ?: true
    val isUrlValid = _urlValidator(data.url)

    return (data.title.isNotEmpty() && isDescriptionValid && isUrlValid)
  }
}