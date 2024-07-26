package com.qubacy.hearit.application._common.exception

import com.qubacy.hearit.application._common.error.ErrorReference

data class HearItException(
    val errorReference: ErrorReference
) : Exception() {

}