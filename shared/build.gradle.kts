plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
    id("dev.icerock.mobile.multiplatform-resources")
    id("com.squareup.sqldelight")
}

version = "1.0-SNAPSHOT"
val ktorVersion = extra["ktor.version"]

kotlin {
    android()
    ios()
    iosSimulatorArm64()

    cocoapods {
        summary = "Shared code for the sample"
        homepage = "https://appkickstarter.com"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
//            export("com.mohamedrejeb.calf:calf-ui:0.1.1")
        }
        extraSpecAttributes["resources"] =
            "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }

    val napierVersion = "2.6.1"
    val ktorVersion = "2.2.4"
    val compose_image = "1.2.10"
    val koin_core_version = "3.4.0"
    val koin_android_version = "3.3.3"
    val koin_android_compose_version = "3.4.2"
    val voyagerVersion = "1.0.0-rc04"
    val mokoResourcesVersion = "0.23.0"

    sourceSets {
        val commonMain by getting {
            dependencies {

                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

                api("io.github.aakira:napier:$napierVersion")

                api("com.russhwolf:multiplatform-settings:1.0.0")

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
                implementation("org.jetbrains.compose.components:components-resources:1.3.0-beta04-dev879")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
                api("io.github.qdsfdhvh:image-loader:$compose_image")

                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

                api("io.insert-koin:koin-core:$koin_core_version")

                implementation("dev.icerock.moko:resources:$mokoResourcesVersion")
                implementation("dev.icerock.moko:resources-compose:$mokoResourcesVersion")

                api("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
                api("cafe.adriel.voyager:voyager-transitions:$voyagerVersion")
                implementation("cafe.adriel.voyager:voyager-koin:$voyagerVersion")

                // kti
                implementation(libs.sqlDelight.coroutinesExt)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                api(libs.touchlab.kermit)
                implementation(libs.kotlinx.serialization.json)

                implementation(platform("com.aallam.openai:openai-client-bom:3.6.1"))
                implementation("com.aallam.openai:openai-client")

                implementation(libs.bundles.ktor.common)
                implementation(libs.touchlab.stately)

//                // For Adaptive UI components
//                implementation("com.mohamedrejeb.calf:calf-ui:0.1.1")
//
//// For Adaptive FilePicker
//                implementation("com.mohamedrejeb.calf:calf-file-picker:0.1.1")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.appcompat:appcompat:1.5.1")
                implementation("androidx.core:core-ktx:1.9.0")
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")

                api("io.insert-koin:koin-android:$koin_android_version")
                // Jetpack WorkManager
                api("io.insert-koin:koin-androidx-workmanager:$koin_android_version")
                // Navigation Graph
                api("io.insert-koin:koin-androidx-navigation:$koin_android_version")
                // Compose
                api("io.insert-koin:koin-androidx-compose:$koin_android_compose_version")

//                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.sqlDelight.android)
                implementation(libs.ktor.client.okHttp)
                implementation("io.ktor:ktor-client-okhttp")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
            }
        }
        val iosTest by getting
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Test by getting {
            dependsOn(iosTest)
        }
    }
}

android {
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDir("src/commonMain/resources")
    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "com.appkickstarter.shared" // required
    multiplatformResourcesClassName = "SharedRes" // optional, default MR
    //multiplatformResourcesVisibility = MRVisibility.Internal // optional, default Public
    iosBaseLocalizationRegion = "en" // optional, default "en"
    multiplatformResourcesSourceSet = "commonMain"  // optional, default "commonMain"
}

sqldelight {
    database("KaMPKitDb") {
        packageName = "co.touchlab.kampkit.db"
    }
}
