package com.sdk.ipassplussdk.core

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.sdk.ipassplussdk.R
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
                        if (response.isSuccessful) {
                            completion.onSuccess(response.body())
                        } else {
                            try {
//                                if (response.body() != null && !response.body()?.message.equals("")) {
//                                    completion.onError(response.body()?.message.toString())
//                                } else if (response.message().isNullOrEmpty()) {
                                        val errBody = Gson().fromJson(response.errorBody()?.string(), ErrorBodyResponse::class.java)
                                        completion.onError(errBody?.message!!)
//                                }
//                            else {
//                                    completion.onError(response.message())
//                                }
                            }catch (e:Exception){
                                e.printStackTrace()
                                completion.onError(context.getString(R.string.data_processing_error))
                            }
                        }
                    }

                    override fun onFailure(call: Call<UploadDataResponse>, t: Throwable) {
                        completion.onError(t.message.toString())
                    }
                })
        } else {
            completion.onError(context.getString(R.string.internet_connection_not_found))
        }

    }
}