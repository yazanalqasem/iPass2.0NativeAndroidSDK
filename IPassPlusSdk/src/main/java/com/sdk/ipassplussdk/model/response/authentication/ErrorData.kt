package com.sdk.ipassplussdk.model.response.authentication

import com.google.gson.annotations.SerializedName

class ErrorData {
    @SerializedName("error")
    var errorCodeResponse : ErrorCodeResponse? = null

}