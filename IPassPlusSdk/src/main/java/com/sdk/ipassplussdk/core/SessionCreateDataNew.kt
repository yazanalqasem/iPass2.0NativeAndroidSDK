package com.sdk.ipassplussdk.core
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.sdk.ipassplussdk.apis.ApiClient
import com.sdk.ipassplussdk.apis.ApiInterface
import com.sdk.ipassplussdk.apis.ResultListener
import com.sdk.ipassplussdk.model.request.create_aws_session.SessionCreateRequestNew
import com.sdk.ipassplussdk.model.response.create_aws_session.SessionCreateResponseNew
import com.sdk.ipassplussdk.utils.Constants
import com.sdk.ipassplussdk.utils.InternetConnectionService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SessionCreateDataNew {
    @RequiresApi(Build.VERSION_CODES.O)
    fun createSession(
        context: Context,
        token: String,
        request: SessionCreateRequestNew,
        completion: ResultListener<SessionCreateResponseNew>
    ) {
        if (InternetConnectionService.networkAvailable(context)) {
            ApiClient("")?.create(ApiInterface::class.java)!!
                .createSession(token, request).enqueue(object :
                    Callback<SessionCreateResponseNew>{
                    override fun onResponse(
                        call: Call<SessionCreateResponseNew>,
                        response: Response<SessionCreateResponseNew>
                    ) {
                        if (response.isSuccessful) {
                            completion.onSuccess(response.body()!!)
                        } else {
                            try {
                                completion.onError(response.message())
                            }catch (e: Exception){
                                completion.onError("")
                            }
                        }
                    }

                    override fun onFailure(call: Call<SessionCreateResponseNew>, t: Throwable) {
                        completion.onError(t.message.toString())
                    }
                })
        } else {
            completion.onError(Constants.NO_INTERNET_TEXT)
        }

    }
}