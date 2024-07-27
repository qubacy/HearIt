package com.qubacy.hearit.application.ui.visual.resource.error

import com.qubacy.hearit.R
import com.qubacy.hearit.application._common.error.ErrorEnum
import com.qubacy.hearit.application._common.error.ErrorReference

enum class HearItErrorEnum(
    val error: HearItError
) {
    RADIO_INPUT_VALIDATION_ERROR(
        HearItError(
            ErrorEnum.RADIO_INPUT_VALIDATION_ERROR.reference.id,
            R.string.error_message_radio_input_validation_error,
            false
        )
    ),
    RADIO_NOT_FOUND(
        HearItError(
            ErrorEnum.RADIO_NOT_FOUND.reference.id,
            R.string.error_message_radio_not_found,
            false
        )
    );

    companion object {
        fun fromReference(
            reference: ErrorReference
        ): HearItErrorEnum {
            return entries.firstOrNull {
                it.error.id == reference.id
            } ?: throw IllegalArgumentException()
        }
    }
}