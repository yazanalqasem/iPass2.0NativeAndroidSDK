package com.sdk.ipassplussdk.model.response.create_aws_session

import com.google.gson.annotations.SerializedName

data class SessionCreateResponseNew (

    @SerializedName("status"    ) var status    : String? = null,
    @SerializedName("sessionId" ) var sessionId : String? = null

)