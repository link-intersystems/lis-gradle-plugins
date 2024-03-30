rootProject.name = "lis-gradle-plugins"

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.link-intersystems.gradle.multi-module") version "0.2.0"

}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    includeBuild(".")
}