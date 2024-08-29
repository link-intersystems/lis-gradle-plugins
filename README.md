![Java CI with Maven](https://github.com/link-intersystems/lis-gradle-plugins/workflows/Java%20CI%20with%20Gradle/badge.svg)

This repository contains general purposes Gradle plugins that can help you to set up and manage your Gradle projects more efficient.  

# Plugins


| Plugin                                                         | Description                                                                                                | Artifacts                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
|----------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Git Plugin](git-plugin/README.md)                             | Provides extension to Gradle projects so that you can access git information and use JGit's procelain api. | [![Gradle Plugin Portal Version](https://img.shields.io/gradle-plugin-portal/v/com.link-intersystems.gradle.git)](https://plugins.gradle.org/plugin/com.link-intersystems.gradle.git) [![Maven Central Version](https://img.shields.io/maven-central/v/com.link-intersystems.gradle.git/com.link-intersystems.gradle.git.gradle.plugin)](https://mvnrepository.com/artifact/com.link-intersystems.gradle.git)                                              |
| [Multi Module Plugin](multi-module-plugin/README.md)           | Configures the Gradle build by automatically detecting subprojects and composite builds.                   | [![Gradle Plugin Portal Version](https://img.shields.io/gradle-plugin-portal/v/com.link-intersystems.gradle.multi-module)](https://plugins.gradle.org/plugin/com.link-intersystems.gradle.multi-module) [![Maven Central Version](https://img.shields.io/maven-central/v/com.link-intersystems.gradle.multi-module/com.link-intersystems.gradle.multi-module.gradle.plugin)](https://mvnrepository.com/artifact/com.link-intersystems.gradle.multi-module) |
| [Publication Utils Plugin](publication-utils-plugin/README.md) | A collection of utility tasks to deal with publications related stuff like checking if publications are already published to a remote repository.                                                                                              | [![Gradle Plugin Portal Version](https://img.shields.io/gradle-plugin-portal/v/com.link-intersystems.gradle.publication-utils)](https://plugins.gradle.org/plugin/com.link-intersystems.gradle.publication-utils) [![Maven Central Version](https://img.shields.io/maven-central/v/com.link-intersystems.gradle.publication-utils/com.link-intersystems.gradle.publication-utils.gradle.plugin)](https://mvnrepository.com/artifact/com.link-intersystems.gradle.publication-utils)                                                                                                                                                                                         |

# Usage

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