package com.sdk.ipassplussdk.ui

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.ui.liveness.ui.FaceLivenessDetector
import com.amplifyframework.ui.liveness.ui.LivenessColorScheme

object FaceScannerData {

    @RequiresApi(Build.VERSION_CODES.O)
    fun configureFaceScanner(context: Context, callback: (String) -> Unit) {
        try {
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            Amplify.configure(context)
            callback.invoke("FaceScannerConfigured")
        } catch (e: AmplifyException) {
//            Log.e("AmplifyException## ","AmplifyException  -  ${e.message}")
//            Log.e("AmplifyException## ","AmplifyException  -  ${e.recoverySuggestion}")
//            Log.e("AmplifyException## ","AmplifyException  -  ${e.localizedMessage}")
            callback.invoke("Error Initializing Face Scanner")
        }
    }
    fun initFaceDetector(context: Context, sessionId: String, bindView:ViewGroup, callback: (String) -> Unit) {

        val composeView = ComposeView(context).apply {
            setContent {
                MaterialTheme(
                    colorScheme = LivenessColorScheme.default()
                ) {
                    FaceLivenessDetector(
                        sessionId = sessionId,
                        region = "us-east-1",
                        disableStartView = true,
                        onComplete = {
                            (context as Activity).getWindow()
                                .clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            bindView.removeView(this as ComposeView)
                            bindView.removeViewInLayout(this.rootView)
                            callback.invoke("success")
                        },
                        onError = { error ->
                            (context as Activity).getWindow()
                                .clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            callback.invoke("Error during Face Liveness flow")
                        }
                    )
                }
            }
        }
        (context as Activity).getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );

        bindView.addView(composeView) // Add ComposeView to parentViewGroup
    }
}
