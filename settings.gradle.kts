rootProject.name = "lis-gradle-plugins"


pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
    includeBuild("build-logic")
}


plugins {
    id("com.link-intersystems.gradle.multi-module") version "0.5.4"
    id("com.link-intersystems.gradle.publication-utils") version "0.5.4" apply false
}


dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}


dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("${rootDir}/libs.versions.toml"))
        }
    }
}