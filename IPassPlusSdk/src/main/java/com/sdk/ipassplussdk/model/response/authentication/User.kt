package com.sdk.ipassplussdk.model.response.authentication

data class User(
    val email: String,
    val token: String,
    val user_id: String
)