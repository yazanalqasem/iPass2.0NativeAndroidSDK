package com.sdk.ipassplussdk.model.response.aml_manual

data class IndiaSecuritiesExchangeBoardOfIndiaDebarredEntities(
    val aml_types: List<String>,
    val listing_started_utc: String,
    val name: String,
    val url: String
)