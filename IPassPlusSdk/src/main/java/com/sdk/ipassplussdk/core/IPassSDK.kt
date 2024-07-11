package com.sdk.ipassplussdk.core

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.gson.Gson
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
import org.json.JSONObject
import java.net.URL
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
                getIPAddress(
                    context,
                    appToken,
                    email,
                    socialMediaEmail,
                    phoneNumber,
                    flowId,
                    userToken,
                    bindingView,
                    callback
                )

//                if (flowId.equals("10015")) {
//                    uploadData(
//                        context,
//                        appToken,
//                        email,
//                        socialMediaEmail,
//                        phoneNumber,
//                        getIPAddress(true),
//                        flowId,
//                        "Android v2.12",
//                        "0",
//                        callback
//                    )
//                } else {
//                    faceSessionCreateRequest(context, email, userToken, appToken,socialMediaEmail,phoneNumber,
//                        getIPAddress(true),
//                        flowId,"Android v2.12", bindingView, callback)
//                }
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
        Log.e("@@@@@", sessionId)
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
                Log.e("@@@message", exception)
//                val obj = JSONObject(exception)
////
//                val value = obj.getString("message")
//                Log.e("@@@message", value)
                callback.invoke(false, exception)
            }
        })
    }



    //    returns a list of available Processing Scenarios
    fun getScenariosList() {
        val list = arrayListOf(
            "10031",
            "10032",
            "10011",
            "10015",
            )
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
    @RequiresApi(Build.VERSION_CODES.O)
    fun getIPAddress(
        context: Context,
        appToken: String,
        email: String,
        socialMediaEmail: String,
        phoneNumber: String,
        flowId: String,
        userToken: String,
        bindingView: ViewGroup,
        callback: (status: Boolean, message: String) -> Unit
    ): String {
        var ip: String? = null
        val thread = Thread {
            try {
                val url = URL("https://api.ipify.org")
                val connection = url.openConnection()
                connection.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0"
                ) // Set a User-Agent to avoid HTTP 403 Forbidden error
                val inputStream = connection.getInputStream()
                val s = java.util.Scanner(inputStream, "UTF-8").useDelimiter("\\A")
                ip = s.next()


                if (flowId.equals("10015")) {
//                    Log.e("IPPPPP", ip.toString())
                    uploadData(
                        context,
                        appToken,
                        email,
                        socialMediaEmail,
                        phoneNumber,
                        ip.toString(),
                        flowId,
//                        "androidSdk",
                        "Android v2.13",
                        "0",
                        callback
                    )
                } else {
                    faceSessionCreateRequest(
                        context, email, userToken, appToken, socialMediaEmail, phoneNumber,
                        ip.toString(),
//                        flowId, "androidSdk", bindingView, callback
                        flowId, "Android v2.13", bindingView, callback
                    )
                }
            } catch (e: Exception ) {
                e.printStackTrace();
                callback.invoke(false, e.message.toString())
            }
        }

        thread.start();
//        Log.e("IPPPPPPPPPPPP", ip.toString())
        return ip.toString()
    }

}
