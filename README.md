![Java CI with Maven](https://github.com/link-intersystems/lis-gradle-plugins/workflows/Java%20CI%20with%20Gradle/badge.svg)

This repository contains gradle plugins that should help you to set up and 
build projects easier.

# Plugins

All plugins in this repository are available in the maven central repository.
You might need to add `mavenCentral()` to your `pluginManagement`
before the `plugins` configuration to use them.

```kotlin
// settngs.gradle.kts
pluginManagement {
  repositories {
    mavenCentral()
  }
}

plugins {
}
```
# Plugin List


| Plugin                                                   | Description                                                                                                | Artifacts                                                                                                                                                                                                                                          |
|----------------------------------------------------------|------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Gradle Git Plugin](git-plugin/README.md)                | Provides extension to Gradle projects so that you can access git information and use JGit's procelain api. | [![Maven Central Version](https://img.shields.io/maven-central/v/com.link-intersystems.gradle.git/com.link-intersystems.gradle.git.gradle.plugin)](https://mvnrepository.com/artifact/com.link-intersystems.gradle.git)                            |
| [Gradle Multi Module Plugin](settings-plugins/README.md) | Automatically detects subprojects and composite builds and configures the Gradle build.                    | [![Maven Central Version](https://img.shields.io/maven-central/v/com.link-intersystems.gradle.multi-module/com.link-intersystems.gradle.multi-module.gradle.plugin)](https://mvnrepository.com/artifact/com.link-intersystems.gradle.multi-module) |