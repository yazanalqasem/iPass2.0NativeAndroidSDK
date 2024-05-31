package com.sdk.ipassplussdk.core

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.sdk.ipassplussdk.resultCallbacks.InitializeDatabaseCompletion
import com.sdk.ipassplussdk.ui.FaceScannerData
import com.sdk.ipassplussdk.ui.InitializeDatabase
import com.sdk.ipassplussdk.utils.Constants
import com.sdk.ipassplussdk.utils.InternetConnectionService
import com.sdk.ipassplussdk.views.ProgressManager

object DataBaseDownloading {

    //    initialize database for scanning (needs to be initialized at least once before scanning)
    @RequiresApi(Build.VERSION_CODES.O)
    fun initialization(context: Context, completion: InitializeDatabaseCompletion){
        if (!InternetConnectionService.networkAvailable(context)) {
            completion.onCompleted(false, Constants.NO_INTERNET_TEXT)
            return
        }

        ProgressManager.showProgress(context)

        InitializeDatabase.InitDatabase(context, object : InitializeDatabaseCompletion {
            override fun onProgressChanged(progress: Int) {
                completion.onProgressChanged(progress)
            }

            override fun onCompleted(status: Boolean, message: String?) {
                if (status) {
                    Log.e("onCompleted## ",message!!)
                    configureFaceScanner(context, completion)
                } else {
                    Log.e("onCompleted##1 ",message!!)
                    ProgressManager.dismissProgress()
                    completion.onCompleted(status, message)
                }
            }

        })
    }

    //    Face scanner configuration
    @RequiresApi(Build.VERSION_CODES.O)
    private fun configureFaceScanner(
        context: Context,
        completion: InitializeDatabaseCompletion
    ) {
        FaceScannerData.configureFaceScanner(context) {
            Log.e("configureFaceScanner## ","configureFaceScanner")
            ProgressManager.dismissProgress()
            Log.e("dismissProgress## ","dismissProgress")
            if (it.equals("FaceScannerConfigured")) {
                Log.e("FaceScannerConfigured## ","FaceScannerConfigured")
                completion.onCompleted(true, "Database Initialized Successfully")
            } else {
//                completion.onCompleted(false, it)
            }
        }
    }
}