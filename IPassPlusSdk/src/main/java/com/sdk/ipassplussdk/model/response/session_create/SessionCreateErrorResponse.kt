package com.sdk.ipassplussdk.model.response.session_create

import com.google.gson.annotations.SerializedName

data class SessionCreateErrorResponse (

  @SerializedName("status"  ) var status  : Boolean? = null,
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