package com.qubacy.hearit.application.domain._common.model

data class RadioDomainModel(
    val id: Long,
    val title: String,
    val description: String? = null,
    val cover: String? = null,
    val url: String
) {

}