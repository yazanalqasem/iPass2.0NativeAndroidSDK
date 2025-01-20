package com.sdk.ipassplussdk.utils

object ServerUrls {
//    live
    const val base_url = "https://plusapi.ipass-mena.com/api/v1/"
  //  const val base_url = "https://ipassplus.csdevhub.com/api/v1/"

//    local
//    const val base_url = "http://192.168.11.48:4088/api/v1/"

    const val url_authentication = "ipass/create/authenticate/login"
    const val url_customer_access = "ipass/customer/access"
    const val url_aws_session_create = "ipass/plus/face/session/create"
    const val url_upload_data = "ipass/initiate/process/sdk"
    const val url_transaction_detail = "ipass/idv/getAll/data"
}
