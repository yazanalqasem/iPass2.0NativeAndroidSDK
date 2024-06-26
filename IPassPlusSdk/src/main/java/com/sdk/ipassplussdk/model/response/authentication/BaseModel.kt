package com.sdk.ipassplussdk.model.response.authentication

class BaseModel<T> {

    var status: Boolean? = null
    var message: String? = null
    var data: T? = null
    var error: ErrorModel? = null

}