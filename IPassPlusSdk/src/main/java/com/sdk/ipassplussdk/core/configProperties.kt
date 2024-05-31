package com.sdk.ipassplussdk.core

import com.regula.documentreader.api.DocumentReader
import com.regula.documentreader.api.params.AuthenticityParams

object configProperties {

    fun needHologramDetection(value:Boolean){
        val authenticityParams = AuthenticityParams.defaultParams()

        authenticityParams.useLivenessCheck = value
        authenticityParams.livenessParams?.checkHolo = value
        authenticityParams.livenessParams?.checkED = value
        authenticityParams.livenessParams?.checkMLI = value
        authenticityParams.livenessParams?.checkOVI = value

        DocumentReader.Instance().processParams().authenticityParams = authenticityParams
        DocumentReader.Instance().processParams().checkHologram = value
    }

}