package com.sdk.ipassplussdk.model.response.errorbody

import com.google.gson.annotations.SerializedName

data class ErrorBodyResponse (

  @SerializedName("message" ) var message : String? = null

)