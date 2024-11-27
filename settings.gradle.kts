pluginManagement {
    repositories {
        google()  // Google's Maven repository
        mavenCentral()  // Maven Central
        maven { url = uri("https://jitpack.io") }  // JitPack repository
    }
}

dependencyResolutionManagement {
    repositories {
        google()  // Google's Maven repository
        mavenCentral()  // Maven Central
        maven { url = uri("https://jitpack.io") }  // JitPack repository
    }
}

rootProject.name = "Observer"
include(":app")
 