package com.sdk.ipassplussdk.core

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.gson.JsonParser
import com.sdk.ipassplussdk.apis.ResultListener
import com.sdk.ipassplussdk.model.request.authentication.AuthenticationRequest
import com.sdk.ipassplussdk.model.request.create_aws_session.SessionCreateRequestNew
import com.sdk.ipassplussdk.model.request.initiate_data.UploadDataRequest
import com.sdk.ipassplussdk.model.response.authentication.AuthenticationResponse
import com.sdk.ipassplussdk.model.response.consumption.CustomerAccessResponse
import com.sdk.ipassplussdk.model.response.create_aws_session.SessionCreateResponseNew
import com.sdk.ipassplussdk.model.response.initiate_data.UploadDataResponse
import com.sdk.ipassplussdk.model.response.transaction_details.TransactionDetailResponse
import com.sdk.ipassplussdk.ui.DocumentReaderData
import com.sdk.ipassplussdk.ui.FaceScannerData.initFaceDetector
import com.sdk.ipassplussdk.utils.Constants
import com.sdk.ipassplussdk.utils.InternetConnectionService
import com.sdk.ipassplussdk.utils.Scenarios
import com.sdk.ipassplussdk.views.ProgressManager
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections
import java.util.Locale
import java.util.UUID


object iPassSDKManger {

    private var sid = ""
    private var rawResult: String? = null


    //    authentication
    @RequiresApi(Build.VERSION_CODES.O)
    fun UserOnboardingProcess(
        context: Context,
        email: String?,
        password: String?,
        completion: ResultListener<AuthenticationResponse>) {

        if (!InternetConnectionService.networkAvailable(context)) {
            completion.onError(Constants.NO_INTERNET_TEXT)
            return
        }

        if (email.isNullOrEmpty()) {
            completion.onError("Email is required")
            return
        }

        if (password.isNullOrEmpty()) {
            completion.onError("Password is required")
            return
        }

        ProgressManager.showProgress(context)
        val request = AuthenticationRequest(email, password)
        AuthData.auth(context, request, object : ResultListener<AuthenticationResponse> {
            override fun onSuccess(response: AuthenticationResponse?) {
                ProgressManager.dismissProgress()
                completion.onSuccess(response)
            }

            override fun onError(exception: String) {
                ProgressManager.dismissProgress()
                completion.onError(exception)
            }

        })
    }



    //    Show scanner for document verification
    @RequiresApi(Build.VERSION_CODES.O)
    fun startScanningProcess(
        context: Context,
        email: String?,
        userToken: String?,
        appToken: String?,
        socialMediaEmail: String = "",
        phoneNumber: String = "",
        flowId: String?,
        bindingView: ViewGroup?,
        callback: (status: Boolean, message: String) -> Unit
    ) {
        if (!InternetConnectionService.networkAvailable(context)) {
            callback.invoke(false, Constants.NO_INTERNET_TEXT)
            return
        }
        if (email.isNullOrEmpty()) {
            callback.invoke(false, "Email is required")
            return
        }
        if (userToken.isNullOrEmpty()) {
            callback.invoke(false, "User Token is required")
            return
        }
        if (appToken.isNullOrEmpty()) {
            callback.invoke(false, "App Token is required")
            return
        }
        if (flowId.isNullOrEmpty()) {
            callback.invoke(false, "Flow ID is required")
            return
        }
        if ((flowId == "10031") && socialMediaEmail.isNullOrEmpty()) {
            callback.invoke(false, "Social Media Email is required")
            return
        }
        if ((flowId == "10031") && phoneNumber.isNullOrEmpty()) {
            callback.invoke(false, "Phone Number is required")
            return
        }
        if (bindingView == null) {
            callback.invoke(false, "binding view group is null")
            return
        }


        ProgressManager.showProgress(context)
        Consumption.checkAccess(context, appToken, object : ResultListener<CustomerAccessResponse> {
            override fun onSuccess(response: CustomerAccessResponse?) {
                if (response?.message.equals("sucess")) {
                    showDocScanner(context, appToken, userToken, email, socialMediaEmail, phoneNumber, flowId, bindingView, callback)
                } else {
                    ProgressManager.dismissProgress()
                    callback.invoke(false, response?.message!!)
                }
            }

            override fun onError(exception: String) {
                ProgressManager.dismissProgress()
                callback.invoke(false, exception)
            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDocScanner(
        context: Context,
        appToken: String,
        userToken: String,
        email: String,
        socialMediaEmail: String,
        phoneNumber: String,
        flowId: String,
        bindingView: ViewGroup,
        callback: (status: Boolean, message: String) -> Unit
    ) {
        sid = getSid()
        DocumentReaderData.showScanner(context) {
                status, message ->
            if (status) {

                this.rawResult = message

                if (flowId.equals("10015")) {
                    uploadData(
                        context,
                        appToken,
                        email,
                        socialMediaEmail,
                        phoneNumber,
                        getIPAddress(false),
                        flowId,
                        "androidSdk",
                        "0",
                        callback
                    )
                } else {
                    faceSessionCreateRequest(context, email, userToken, appToken,socialMediaEmail,phoneNumber,
                        getIPAddress(false),
                        flowId,"androidSdk", bindingView, callback)
                }
            } else {
                ProgressManager.dismissProgress()
                callback.invoke(false, message)
            }
        }
    }

    //    init face detection
    @RequiresApi(Build.VERSION_CODES.O)
    private fun faceSessionCreateRequest(
        context: Context,
        email: String,
        userToken: String,
        appToken: String,
        socialMediaEmail: String,
        phoneNumber: String,
        ipadd: String,
        flowId: String,
        source: String,
        bindingView: ViewGroup,
        callback: (Boolean, String) -> Unit
    ) {
        if (!InternetConnectionService.networkAvailable(context)) {
            return
        }

        val request = SessionCreateRequestNew()
        request.email = email
        request.authToken = userToken

        SessionCreateDataNew.createSession(
            context,
            appToken,
            request,
            object : ResultListener<SessionCreateResponseNew> {
                override fun onSuccess(response: SessionCreateResponseNew?) {
                    val sessionId = response?.sessionId!!

                    showFaceScanner(
                        context = context,
                        email = email,
                        userToken = userToken,
                        appToken = appToken,
                        socialMediaEmail = socialMediaEmail,
                        phoneNumber = phoneNumber,
                        ipadd = ipadd,
                        flowId = flowId,
                        source = source,
                        sessionId = sessionId,
                        bindingView = bindingView,
                        callback = callback
                    )

                }
                override fun onError(exception: String) {
                    ProgressManager.dismissProgress()
                    callback.invoke(false, exception)
                }
            })
    }

    //    show scanner to detect face liveness
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showFaceScanner(
        context: Context,
        email: String,
        userToken: String,
        appToken: String,
        socialMediaEmail: String,
        phoneNumber: String,
        ipadd: String,
        flowId: String,
        source: String,
        sessionId: String,
        bindingView: ViewGroup,
        callback: (Boolean, String) -> Unit
    ) {
        ProgressManager.dismissProgress()
        initFaceDetector(context, sessionId, bindingView) {
            if (it.equals("success")) {
                    ProgressManager.showProgress(context)
                    uploadData(
                        context,
                        appToken,
                        email,
                        socialMediaEmail,
                        phoneNumber,
                        ipadd,
                        flowId,
                        source,
                        sessionId,
                        callback
                    )
                } else {
                ProgressManager.dismissProgress()
                callback.invoke(false, it)
            }}
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadData(
        context: Context,
        token: String,
        userEmail: String,
        socialMediaEmail: String,
        phoneNumber: String,
        ipadd: String,
        flowId: String,
        source: String,
        sessionId: String,
        callback: (Boolean, String) -> Unit
    ) {
        val uploaddataRequest = UploadDataRequest()
        uploaddataRequest.email = userEmail
        uploaddataRequest.randomid = sid
        uploaddataRequest.socialMediaEmail = socialMediaEmail
        uploaddataRequest.phoneNumber= phoneNumber
        uploaddataRequest.ipadd= ipadd
        uploaddataRequest.workflow= flowId
        uploaddataRequest.source= source
        uploaddataRequest.sessionId = sessionId
        val rawData = JsonParser().parse(rawResult).asJsonObject
        uploaddataRequest.idvData = rawData

        InitiateData.uploadData(context, token,uploaddataRequest, object : ResultListener<UploadDataResponse> {
            override fun onSuccess(response: UploadDataResponse?) {
                ProgressManager.dismissProgress()
                callback.invoke(true, response?.message!!)
            }
            override fun onError(exception: String) {
                ProgressManager.dismissProgress()
                callback.invoke(false, exception)
            }
        })
    }



    //    returns a list of available Processing Scenarios
    fun getScenariosList() {
        val list = arrayListOf(Scenarios.SCENARIO_FULL_PROCESS)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getDocumentScannerData(
        context: Context,
        appToken: String,
        completion: ResultListener<TransactionDetailResponse>) {
        if (!InternetConnectionService.networkAvailable(context)) {
            completion.onError(Constants.NO_INTERNET_TEXT)
            return
        }
        if (appToken.isNullOrEmpty()) {
            completion.onError("App Token is required")
            return
        }

        ProgressManager.showProgress(context)
        TransactionDetail.transactionDetails(context, appToken, sid, object : ResultListener<TransactionDetailResponse> {
            override fun onSuccess(response: TransactionDetailResponse?) {
                ProgressManager.dismissProgress()
                completion.onSuccess(response)
            }

            override fun onError(exception: String) {
                ProgressManager.dismissProgress()
                completion.onError(exception)
            }
        })
    }


    //    returns a unique sId for every scan
    private fun getSid(): String {
        // Generate a random UUID
        val myUuid = UUID.randomUUID()
        val myUuidAsString = myUuid.toString()
        Log.e("Sid", myUuidAsString)

        return myUuidAsString
    }


    /**
     * Get IP address from first non-localhost interface
     * @param useIPv4   true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    fun getIPAddress(useIPv4: Boolean): String {
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs: List<InetAddress> = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        val isIPv4 = sAddr.indexOf(':') < 0

                        if (useIPv4) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                return if (delim < 0) sAddr.uppercase(Locale.getDefault()) else sAddr.substring(
                                    0,
                                    delim
                                ).uppercase(
                                    Locale.getDefault()
                                )
                            }
                        }
                    }
                }
            }
        } catch (ignored: Exception) {
        } // for now eat exceptions

        return ""
    }

}
