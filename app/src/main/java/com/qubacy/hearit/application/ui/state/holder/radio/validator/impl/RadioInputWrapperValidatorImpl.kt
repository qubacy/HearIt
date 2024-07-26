package com.qubacy.hearit.application.ui.state.holder.radio.validator.impl

import com.qubacy.hearit.application.ui.state.holder.radio.validator._common.RadioInputWrapperValidator
import com.qubacy.hearit.application.ui.state.holder.radio.wrapper.RadioInputWrapper

class RadioInputWrapperValidatorImpl(
  private val _urlValidator: (String) -> Boolean
) : RadioInputWrapperValidator {
  private val _textRegex = Regex("^\\S+.*$")

  override fun validate(data: RadioInputWrapper): Boolean {
    val isTitleValid = data.title.matches(_textRegex)
    val isDescriptionValid = data.description?.let { data.description.matches(_textRegex) } ?: true
    val isUrlValid = _urlValidator(data.url)

    return (isTitleValid && isDescriptionValid && isUrlValid)
  }
}