# IPassPlusSDK_Android

Steps of Using IPass SDK
To explain how a user can use the IPass SDK framework in steps, you can outline the process as follows:

-> Integrate of SDK into App
In this step User Will add the IPass SDK inside the project's write:

    implementation 'com.github.mobileexpert1:IPassPlusSDK_Android:2.0.23'

Add:-

    id ("maven-publish")

Add these lines in your settings.gradle file

    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
            google()
            mavenCentral()
            jcenter()
            maven {
                url =uri("https://maven.regulaforensics.com/RegulaDocumentReader/Beta")
            }
        }
    }

Configure Permissions in manifest file

In this step user will give required permissions in manifest file to enable the necessary device features:

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-feature android:name="android.hardware.camera" />
    <uses-feature android:name="android.hardware.camera.autofocus" />
    <uses-permission android:name="android.permission.NFC" />


*************************************

* Initialise Database:

This method is used to setup database. It will download necessary files required for processing.

        DataBaseDownloading.initialization(this, object: InitializeDatabaseCompletion {
            override fun onProgressChanged(progress: Int) {
                // get progress
            }

            override fun onCompleted(
                status: Boolean,
                message: String?
            ) {
                if (status) {
                    // Show message
                } else {
                    // Show error message
                }
            }

        })


* Get Auth Token :

This method verfies the account credentials and returns a Authorization token which is valid for 30 minutes. This token is required in next steps for authorization.

        iPassSDKManger.UserOnboardingProcess(context, email, password, object : ResultListener<AuthenticationResponse> {
            override fun onSuccess(response: AuthenticationResponse?) {
                val authToken = response?.user?.token!!
                // Get auth token
            }

            override fun onError(exception: String) {
                // show error message
            }
        })


* Get Scenarios List :

This method returns list of scenarios available.

        iPassSDKManger.getScenariosList()


* Show scanner :

This method opens the scanner to scan the document. It uploads the document on server for processing the data.

        iPassSDKManger.startScanningProcess(requireContext(), email, userToken, apptoken, socialMediaEmail, phoneNumber, flowId, ViewGroup) {
            status, message ->
            if (status) {
                // show success message
            } else {
                // show error message message
            }
        }


* Get Document Data :

This Method Returns data scanned from Documents.


        iPassSDKManger.getDocumentScannerData(requireContext(), apptoken, object : ResultListener<TransactionDetailResponse> {
            override fun onSuccess(response: TransactionDetailResponse?) {
                if (response?.Apistatus!!) {
                    //   Get Document Scanner Data
                } else {
                    //   Show error
                }
            }

            override fun onError(exception: String) {
                //   Show error
            }

        })

* Reduce APK Size

To reduce the APK size, follow these steps:

        1. In android studio, Select File > New > New Module from the menu bar. In the Create New Module dialog, select Dynamic Feature Module and click Next.

        2. On the Configure your new module screen, give your module a name(iPassSdk).

        3. On the Configure your new module screen, specify the module title(iPass).

        4. Check the Enable on-demand box. Hit Finish and wait for the project to sync.

        5. Now add the below mentioned line in the dynamic module's (iPassSdk) build gradle file and sync project.

           implementation("com.github.mobileexpert1:iPassPlusCore:1.0.0")

        6. Add these lines in your activity

           private var splitInstallManager: SplitInstallManager? = null

           splitInstallManager = SplitInstallManagerFactory.create(this)

           val request = SplitInstallRequest.newBuilder()
          .addModule(name)
          .build()

           splitInstallManager?.startInstall(request)
            ?.addOnSuccessListener {
           // Packages Installed
           }
           ?.addOnFailureListener {
           // Packages Installation failed!
           }


***************************