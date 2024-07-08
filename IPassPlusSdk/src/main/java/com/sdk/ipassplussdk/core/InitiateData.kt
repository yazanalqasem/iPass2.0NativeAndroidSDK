package com.sdk.ipassplussdk.core

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.sdk.ipassplussdk.apis.ApiClient
import com.sdk.ipassplussdk.apis.ApiInterface
import com.sdk.ipassplussdk.apis.ResultListener
import com.sdk.ipassplussdk.model.request.initiate_data.UploadDataRequest
import com.sdk.ipassplussdk.model.response.errorbody.ErrorBodyResponse
import com.sdk.ipassplussdk.model.response.initiate_data.UploadDataResponse
import com.sdk.ipassplussdk.utils.Constants
import com.sdk.ipassplussdk.utils.InternetConnectionService
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object InitiateData {

    @RequiresApi(Build.VERSION_CODES.O)
    fun uploadData(
        context: Context,
        token: String,
        request: UploadDataRequest,
        completion: ResultListener<UploadDataResponse>
    ) {
        if (InternetConnectionService.networkAvailable(context)) {
            ApiClient("")?.create(ApiInterface::class.java)!!
                .uploadData( token, request ).enqueue(object :
                    Callback<UploadDataResponse> {
                    override fun onResponse(
                        call: Call<UploadDataResponse>,
                        response: Response<UploadDataResponse>
                    ) {
                        println("Response ==> $response")
//                        println("Response ${response.errorBody()?.toString()}")
                        if (response.isSuccessful) {
//                            val rsp = Gson().fromJson(response.body().toString(), UploadDataResponse::class.java)
                            completion.onSuccess(response.body())
//                            response.errorBody()?.string()
                        } else {
                            try {
                                if (response.message().isNullOrEmpty()) {
//                                    Log.e("@@@@@@@T",response.errorBody()?.string()!!)
//                                    Log.e("@@@@@@@T",response.message().toString())

//                                    val c = response.errorBody()?.string().toString()
//                                    val obj = JSONObject(c)
//
//                                    if (obj.has("message")) {
//                                        Log.e("@@@message", obj.getString("message"))
                                        val errBody = Gson().fromJson(response.errorBody()?.string(), ErrorBodyResponse::class.java)
                                        completion.onError(errBody?.message!!)
//                                    } else {
//                                        completion.onError("Data processing error!")
//                                    }
                                } else {
                                    completion.onError(response.message())
                                }
                            }catch (e:Exception){
                                e.printStackTrace()
                                completion.onError("Data processing error!")
                            }
                        }
                    }

                    override fun onFailure(call: Call<UploadDataResponse>, t: Throwable) {
                        completion.onError(t.message.toString())
                    }
                })
        } else {
            completion.onError(Constants.NO_INTERNET_TEXT)
        }

    }
}