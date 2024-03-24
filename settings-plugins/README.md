# Settings Plugins

The settings plugins described here are available in the maven central repository. 
You might need to add `mavenCentral()` to your `pluginManagement` before the `plugins` configuration.

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

# Multi Module Plugin [![Maven Central Version](https://img.shields.io/maven-central/v/com.link-intersystems.gradle.multi-module/com.link-intersystems.gradle.multi-module.gradle.plugin)](https://mvnrepository.com/artifact/com.link-intersystems.gradle.multi-module)

The multi module plugin automatically detects subprojects and composite builds and configures
the gradle build.

- composite build detection

  Each directory that contains a `settings.gradle` or `settings.gradle.kts` is recognized as a composite build.
  Regardless if it also contains a build file.
- subproject detection

  Each directory that contains a `build.gradle` or a `build.gradle.kts` is recognized as a subproject. If the
  directory also contains a settings file it is recognized as a composite build.

## plugin configuration

Add the multi module plugin to your root project settings file.

```kotlin
// settngs.gradle.kts
plugins {
    id("com.link-intersystems.gradle.multi-module") version "0.2"
}
```

### exclude paths

You can add the `com.link-intersystems.gradle.multi-module.exclude-paths` gradle property to
exclude paths from detection.

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

Here are some examples
```properties
# gradle.properties beside the settings.gradle.kts

# exclude only modules/moduleA
com.link-intersystems.gradle.multi-module.exclude-paths = modules/moduleA

# exclude all modules named moduleA
com.link-intersystems.gradle.multi-module.exclude-paths = **/moduleA

# exclude modules/moduleA and modules/moduleB
com.link-intersystems.gradle.multi-module.exclude-paths = regex:modules/module?
```
Under the hood the multi module plugin uses Java's PathMatcher, so you can 
configure whatever a [PathMatch](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/nio/file/FileSystem.html#getPathMatcher(java.lang.String)) can be configured with.

If you do not prefix the exclude path with `glob:` or `regex:`, the plugin assumes
that the path is a glob pattern.

### default excludes

Per default the plugin excludes `buildSrc` and any includeBuild of a pluginManagement,
since these locations are usually used to implement convention plugins.

However, you can turn off the default excludes
```properties
# gradle.properties beside the settings.gradle.kts

com.link-intersystems.gradle.multi-module.omit-default-excludes = true
```