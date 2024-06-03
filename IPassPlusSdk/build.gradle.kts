plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
//    id("com.google.gms.google-services")

}

var artifactId = "IPassPlusSDK"
var groupId = "com.sdk.ipassplussdk"

android {
    namespace = "com.sdk.ipassplussdk"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
//        resConfigs("en")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
//        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    packagingOptions {
        jniLibs {
            useLegacyPackaging = true // Enabling flag to compress JNI Libs to reduce APK size Ref: https://developer.android.com/studio/releases/gradle-plugin#compress-native-libs-dsl
        }
    }

}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation(files("libs/api-7.2.9754/api-7.2.9754.aar"))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.regula.documentreader:api:7.2.9754+@aar") {
        this.isTransitive = true
    }
//    implementation("com.github.yazanalqasem:iPass2.0CoreAndroidSDK:2.1")

    // FaceLivenessDetector dependency
    implementation ("com.amplifyframework.ui:liveness:1.2.1")

    // Amplify Auth dependency (unnecessary if using your own credentials provider)
    implementation ("com.amplifyframework:aws-auth-cognito:2.14.5")

    // Material3 dependency for theming FaceLivenessDetector
    implementation ("androidx.compose.material3:material3:1.2.0")

    // Support for Java 8 features
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:1.1.5")

    implementation ("com.amazonaws:aws-android-sdk-core:2.16.0")
    implementation ("com.amazonaws:aws-android-sdk-rekognition:2.16.0")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.6")
    implementation ("com.google.code.gson:gson:2.8.9")
    implementation ("com.google.android.gms:play-services-vision:20.1.3")
    implementation("com.google.android.material:material:1.11.0")



}
project.afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.sdk.ipassplussdk"
                artifactId = "iPass2.0NativeAndroidSDK"
                version = "2.12"
            }
        }
    }
}