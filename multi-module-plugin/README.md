# Multi Module Plugin [![Gradle Plugin Portal Version](https://img.shields.io/gradle-plugin-portal/v/com.link-intersystems.gradle.multi-module)](https://plugins.gradle.org/plugin/com.link-intersystems.gradle.multi-module) [![Maven Central Version](https://img.shields.io/maven-central/v/com.link-intersystems.gradle.multi-module/com.link-intersystems.gradle.multi-module.gradle.plugin)](https://mvnrepository.com/artifactCoordinates/com.link-intersystems.gradle.multi-module)

The multi module plugin automatically detects subprojects and composite builds and configures
the gradle build.

- composite build detection

  Each directory that contains a `settings.gradle` or `settings.gradle.kts` is recognized as a composite build.
  Regardless if it also contains a build file.
- subproject detection

  Each directory that contains a `build.gradle` or a `build.gradle.kts` is recognized as a subproject. If the
  directory also contains a settings file it is recognized as a composite build.

## Plugin Configuration

Add the multi module plugin to your root project settings file.

```kotlin
// settngs.gradle.kts
plugins {
    id("com.link-intersystems.gradle.multi-module") version "+"  // latest version. Set a specific one
}
```

### Exclude Paths

Let's assume you have a project structure like this.
```
my-app/
├─ modules/
│  ├─ moduleA/
│  │  ├─ build.gradle.kts
│  ├─ moduleB/
│     ├─ build.gradle.kts
├─ otherModules/
│  ├─ moduleA/
│  │  ├─ build.gradle.kts
├─ build.gradle.kts
├─ gradle.properties
├─ settings.gradle.kts
```

You can then exclude specific paths by configuring the `MultiModuleConfig`. Here are some examples:
```kotlin
  // settings.gradle.kts
import com.link_intersystems.gradle.plugins.multimodule.MultiModuleConfig

// Exclude only modules/moduleA
configure<MultiModuleConfig> {
  excludedPaths = listOf("modules/moduleA")
}

// Exclude all modules ending with A using a glob pattern (modules/moduleA, otherModules/moduleA)
configure<MultiModuleConfig> {
  excludedPaths = listOf("**/*A")
}

// Exclude modules/moduleA and modules/moduleB
configure<MultiModuleConfig> {
  excludedPaths = listOf("regex:modules/module?")
}
```
Under the hood the multi-module plugin uses Java's PathMatcher, so you can 
configure whatever a [PathMatcher](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/nio/file/FileSystem.html#getPathMatcher(java.lang.String)) can be configured with.

If you do not prefix the exclude path with `glob:` or `regex:`, the plugin assumes
that the path is a glob pattern.

### Default Excludes

Per default the plugin excludes `buildSrc` and any includeBuild that is configured within
a pluginManagement section, since these locations are usually used for convention plugins.

However, you can turn off the default excludes
```kotlin
  // settings.gradle.kts
import com.link_intersystems.gradle.plugins.multimodule.SettingsMultiModuleConfig

configure<MultiModuleConfig> {
  isOmitDefaultExcludes = true
}
```