package com.qubacy.hearit.application.ui.visual.resource.error

import com.qubacy.hearit.application._common.error.ErrorReference

enum class HearItErrorEnum(
    val error: HearItError
) {
    ;

    companion object {
        fun fromReference(reference: ErrorReference): HearItErrorEnum {
            return entries.firstOrNull {
                it.error.id == reference.id
            } ?: throw IllegalArgumentException()
        }
    }
}