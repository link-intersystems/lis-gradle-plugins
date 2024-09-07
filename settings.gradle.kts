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
        mavenCentral()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("lis") {
            version("lisGradleCommonsVersion", "0.0.6")

            library(
                "platform",
                "com.link-intersystems.gradle.commons",
                "lis-gradle-platform"
            ).versionRef("lisGradleCommonsVersion")

            library(
                "gradleProjectBuilder",
                "com.link-intersystems.gradle.commons",
                "lis-gradle-project-builder"
            ).withoutVersion()

            library("gradleMocking", "com.link-intersystems.gradle.commons", "lis-gradle-mocking").withoutVersion()
        }

        create("assertj"){
            version("assertjVersion", "3.26.3")
            library(
                "core",
                "org.assertj",
                "assertj-core"
            ).versionRef("assertjVersion")
        }

        create("eclipse") {
            library(
                "jgit",
                "org.eclipse.jgit:org.eclipse.jgit:6.9.0.202403050737-r"
            )
        }

        create("junit") {
            library("jupiter-api", "org.junit.jupiter:junit-jupiter-api:5.10.2")
            library("jupiter-engine", "org.junit.jupiter:junit-jupiter-engine:5.10.2")
        }

        create("mockito") {
            library("core", "org.mockito:mockito-core:5.11.0")
        }
        create("slf4j") {
            library("api", "org.slf4j:slf4j-api:2.0.12")
        }
    }
}