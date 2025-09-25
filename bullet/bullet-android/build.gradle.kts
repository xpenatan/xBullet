plugins {
    id("com.android.library")
    kotlin("android")
}

val moduleName = "bullet-android"

android {
    namespace = "bullet"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
    }

    sourceSets {
        named("main") {
            jniLibs.srcDirs("$projectDir/../bullet-build/build/c++/libs/android")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(LibExt.java8Target)
        targetCompatibility = JavaVersion.toVersion(LibExt.java8Target)
    }
    kotlinOptions {
        jvmTarget = LibExt.java8Target
    }
}

dependencies {
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = moduleName
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}