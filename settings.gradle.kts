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
            version("listGradleCommons_version", "0.0.6")
            version("assertj_version", "3.26.3")
            version("jgit_version", "6.9.0.202403050737-r")
            version("junit_jupiter_version", "5.10.2")
            version("mockito_version", "5.11.0")
            version("slf4j_version", "2.0.12")

            library(
                "lis-platform",
                "com.link-intersystems.gradle.commons",
                "lis-gradle-platform"
            ).versionRef("listGradleCommons_version")

            library(
                "lis-gradle-projectBuilder",
                "com.link-intersystems.gradle.commons",
                "lis-gradle-project-builder"
            ).versionRef("listGradleCommons_version")

            library(
                "lis-gradle-mocking",
                "com.link-intersystems.gradle.commons",
                "lis-gradle-mocking"
            )
                .versionRef("listGradleCommons_version")

            library(
                "assertj-core",
                "org.assertj",
                "assertj-core"
            ).versionRef("assertj_version")

            library(
                "eclipse-jgit",
                "org.eclipse.jgit", "org.eclipse.jgit"
            ).versionRef("jgit_version")

            library(
                "junit-jupiter-api",
                "org.junit.jupiter",
                "junit-jupiter-api"
            ).versionRef("junit_jupiter_version")

            library(
                "junit-jupiter-engine",
                "org.junit.jupiter",
                "junit-jupiter-engine"
            ).versionRef("junit.jupiter.version")

            library(
                "mockito-core",
                "org.mockito",
                "mockito-core"
            ).versionRef("mockito_version")

            library(
                "slf4j-api",
                "org.slf4j",
                "slf4j-api"
            ).versionRef("slf4j_version")

            bundle(
                "testing",
                listOf(
                    "junit-jupiter-api",
                    "junit-jupiter-engine",
                    "assertj-core",
                    "mockito-core",
                    "lis-gradle-projectBuilder",
                    "lis-gradle-mocking"
                )
            )
        }
    }
}