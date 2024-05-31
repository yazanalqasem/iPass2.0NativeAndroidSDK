package com.sdk.ipassplussdk.model.request.create_aws_session

import com.google.gson.annotations.SerializedName

data class SessionCreateRequestNew(
@SerializedName("auth_token" ) var authToken : String? = null,
@SerializedName("email"      ) var email     : String? = null

)