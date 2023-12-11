@file:Suppress("DSL_SCOPE_VIOLATION")
plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    kotlin("jvm") apply false
    kotlin("multiplatform") apply false
    kotlin("android") apply false
    id("com.android.application") apply false
    id("com.android.library") apply false
    id("org.jetbrains.compose") apply false
    id("dev.icerock.moko.kswift") apply false // TODO: Check

//    alias(libs.plugins.jvm) apply false
}

tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        mavenLocal()
        gradlePluginPortal()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

buildscript {
    val sqlDelightVersion = "1.5.5"

    repositories {
        gradlePluginPortal()
    }

    dependencies {
        // used to init firebase (https://stackoverflow.com/a/40085096)
        classpath("com.google.gms:google-services:4.3.15")
        classpath("dev.icerock.moko:resources-generator:0.23.0")
        classpath("com.squareup.sqldelight:gradle-plugin:$sqlDelightVersion")
    }
}
