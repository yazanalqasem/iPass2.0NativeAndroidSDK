package com.sdk.ipassplussdk.ui

import android.content.Context
import android.content.res.AssetManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.regula.documentreader.api.DocumentReader
import com.regula.documentreader.api.params.DocReaderConfig
import com.sdk.ipassplussdk.R
import com.sdk.ipassplussdk.resultCallbacks.InitializeDatabaseCompletion
import com.sdk.ipassplussdk.utils.InternetConnectionService
import com.sdk.ipassplussdk.utils.LicenseUtil
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


object InitializeDatabase {

    @Transient
    var isInitializedByBleDevice: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    fun InitDatabase(context: Context, completion: InitializeDatabaseCompletion){
        if (InternetConnectionService.networkAvailable(context)) {
            if (LicenseUtil.readFileFromAssets("SdkLicense", "sdk.license", context) == null
                && !isInitializedByBleDevice
            ) completion.onCompleted(false, "License not found")

            if (DocumentReader.Instance().isReady)
                onInitComplete(completion)

            val license = LicenseUtil.readFileFromAssets(
                "SdkLicense",
                "sdk.license",
                context
            )

            license?.let {
                // Show progress indicator
//                initDialog = showProgressDialog(context, "Initializing")
                initializeReader(it, context, completion)
            }
        } else {
            completion.onCompleted(false, context.getString(R.string.internet_connection_not_found))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeReader(
        license: ByteArray,
        context: Context, completion: InitializeDatabaseCompletion
    ) {
//        DocumentReader.Instance().prepareDatabase(context, object :
//            IDocumentReaderPrepareCompletion {
//            override fun onPrepareProgressChanged(progress: Int) {
//                // getting progress
//                completion.onProgressChanged(progress)
////                Log.e("progressChanged## ",progress.toString())
//            }
//
//            @RequiresApi(Build.VERSION_CODES.O)
//            override fun onPrepareCompleted(status: Boolean, error: DocumentReaderException?) {
//                Log.e("onPrepareCompleted","onPrepareCompleted"+status+"==="+error?.message.toString()+"==="+error.toString())
                // database was prepared



//        val customDbPath = "/storage/emulated/0/Android/data/com.app.ipassplus/JOR_AllPassports"
//        val customDbPath = getFile(context).path
        val config = DocReaderConfig(license)

        DocumentReader.Instance()
            .initializeReader(context, config) {
                                               success, error_initializeReader ->

//                        DocumentReader.Instance().customization().edit().setShowHelpAnimation(false).apply()
//                        Log.e("initialized","initialized"+success+"=="+error_initializeReader.toString())
                if (success) {
                    onInitComplete(completion)
                }
                else {
//                            Log.e("error","error_initializeReader?.message.toString()")
                    completion.onCompleted(false, error_initializeReader?.message.toString())
                }
            }
//            }
//        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onInitComplete(completion: InitializeDatabaseCompletion) {

//        DocumentReader.Instance().processParams().debugSaveLogs = false
//        DocumentReader.Instance().processParams().debugSaveCroppedImages = false
//        DocumentReader.Instance().processParams().debugSaveImages = false

//        val path = DocumentReader.Instance().processParams().sessionLogFolder
//        Log.d("SdkLicense" , "Path: $path")


//        DocumentReader.Instance().setLocalizationCallback { stringId ->
//            if(stringId == "strLookingDocument")
//            // return@setLocalizationCallback SettingsActivity.customString
//                return@setLocalizationCallback ""
//            return@setLocalizationCallback null
//        }
        completion.onCompleted(true,"Success")
//Log.e("completed","completed")
//        FaceScannerData.configureFaceScanner(context, callback)

    }


    private fun getFile(context: Context): File {
        val am: AssetManager = context.getAssets()
        val inputStream = am.open("E:\\AndroidStudioProjects24\\iPass2.0NativeAndroidSDK\\app\\src\\main\\assets\\JOR_AllPassports\\db.dat")
        val file: File? = createFileFromInputStream(inputStream)
        return file!!
    }

    private fun createFileFromInputStream(inputStream: InputStream): File? {
        try {
            val f: File = File("my_file_name")
            val outputStream: OutputStream = FileOutputStream(f)
            val buffer = ByteArray(1024)
            var length = 0

            while ((inputStream.read(buffer).also { length = it }) > 0) {
                outputStream.write(buffer, 0, length)
            }

            outputStream.close()
            inputStream.close()

            return f
        } catch (e: IOException) {
            //Logging exception
        }

        return null
    }

}