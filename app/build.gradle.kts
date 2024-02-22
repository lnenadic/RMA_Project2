plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.nenadic.cdapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nenadic.cdapp"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
    androidComponents {
        onVariants(selector().all()) { variant ->
            afterEvaluate {
                project.tasks.getByName("ksp" + variant.name.capitalize() + "Kotlin") {
                    val dataBindingTask =
                        project.tasks.getByName("dataBindingGenBaseClasses" + variant.name.capitalize()) as (com.android.build.gradle.internal.tasks.databinding.DataBindingGenBaseClassesTask)

                    (this as (org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompileTool<*>)).setSource(dataBindingTask.sourceOutFolder)
                }
            }
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    //Hilt DI
    implementation("com.google.dagger:hilt-android:2.50")
    ksp("com.google.dagger:hilt-android-compiler:2.50")

    //Gide for Image
    implementation("com.github.bumptech.glide:glide:4.11.0")

    //room
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")


    // Kotlin Extensions and Coroutines support for Room
    implementation ("androidx.room:room-ktx:2.6.1")

    implementation("androidx.activity:activity-ktx:1.8.2")


}