rootProject.name = "lis-gradle-plugins"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.link-intersystems.gradle.multi-module") version "0.1"

}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}