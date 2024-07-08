package com.sdk.ipassplussdk.model.response.create_aws_session

import com.google.gson.annotations.SerializedName

data class SessionCreateResponseNew (

    @SerializedName("status"    ) var status    : String? = null,
    @SerializedName("sessionId" ) var sessionId : String? = null,

    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("error"   ) var error   : Error?   = Error()

)

data class User (

    @SerializedName("title"  ) var title  : String? = null,
    @SerializedName("status" ) var status : Int?    = null,
    @SerializedName("detail" ) var detail : String? = null

)

data class Error (

    @SerializedName("user" ) var user : User? = User()

)