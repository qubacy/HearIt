package com.qubacy.hearit.application._common.error

enum class ErrorEnum(val reference: ErrorReference) {
  RADIO_INPUT_VALIDATION_ERROR(ErrorReference(0)),
  RADIO_NOT_FOUND(ErrorReference(1));
}