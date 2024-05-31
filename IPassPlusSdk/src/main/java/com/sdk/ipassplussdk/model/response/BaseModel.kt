package com.sdk.ipassplussdk.model.response

import com.ipasssdk.model.response.ErrorModel

class BaseModel<T> {

    var status: Boolean? = null
    var message: String? = null
    var data: T? = null
    var error: ErrorModel? = null

}