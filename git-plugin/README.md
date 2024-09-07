# Git Plugin [![Gradle Plugin Portal Version](https://img.shields.io/gradle-plugin-portal/v/com.link-intersystems.gradle.git)](https://plugins.gradle.org/plugin/com.link-intersystems.gradle.git) [![Maven Central Version](https://img.shields.io/maven-central/v/com.link-intersystems.gradle.git/com.link-intersystems.gradle.git.gradle.plugin)](https://mvnrepository.com/artifactCoordinates/com.link-intersystems.gradle.git)

The Git plugin provides extension to Gradle projects so that you can access git information
and use [JGit's procelain api](https://archive.eclipse.org/jgit/docs/jgit-3.2.0.201312181205-r/apidocs/org/eclipse/jgit/api/Git.html). Take a look at examples [here](https://github.com/centic9/jgit-cookbook/tree/master/src/main/java/org/dstadler/jgit/porcelain)

## Plugin Usage

```kotlin
//build.gradle.kts
plugins {
    id("com.link-intersystems.gradle.git") version "+"  // latest version. Set a specific one
}
```

## Git Info

You can then access the `gitInfo` extension that is available 
in your `build.gralde.kts` in which you applied the plugin. For details
about the `gitInfo` properties take a look at [`GitInfo`](src/main/java/com/link_intersystems/gradle/plugins/git/GitInfo.java) 

```kotlin
println(gitInfo.shortCommitId)
println(gitInfo.tags)

println("""
commit ${gitInfo.commitId} (HEAD -> ${gitInfo.branch})
Author:     ${gitInfo.authorName} <${gitInfo.authorEmail}>
AuthorDate: ${gitInfo.authorDateTime}
Commit:     ${gitInfo.committerName} <${gitInfo.committerEmail}>
CommitDate: ${gitInfo.commitDateTime}

    ${gitInfo.commitMessage}
""".trimIndent())
```

## "GitPorcelain"-like API

When the plugin is applied to a project, it will create a [`Git`](https://archive.eclipse.org/jgit/docs/jgit-3.2.0.201312181205-r/apidocs/org/eclipse/jgit/api/Git.html) 
object and makes it accessible through the project extension named `git`.

You can then write your own tasks based on that extension.

```kotlin
tasks.create<DefaultTask>("commitAll") {
    val actualCommitMessage = gitInfo.commitMessage

    doLast {
        git.add().addFilepattern(".").call()
        git.commit().setMessage(actualCommitMessage).call()
    }
}
```

## git-info Task

The plugin also adds a `git-info` task to the project that just logs the actual
commit. 

```bash
$ ./gradlew git-info

> Task :settings-plugins:git-info
commit 0c511487dad2d7610cbb43171d86aece24fef6ea (HEAD) -> main

Author:     Ren� Link<rene.link@link-intersystems.com>
AuthorDate: 2024-03-28T07:28:58+01:00[GMT+01:00]
Commit:     Ren� Link<rene.link@link-intersystems.com>
ComitDate:  2024-03-28T07:28:58+01:00[GMT+01:00]

Added git-info task.
```

As you can see there is still a **character encoding issue**.
It doesn't seem to be a problem of the plugin itself rather a general
Gradle issue. Take a look at my 
[stackoverflow question](https://stackoverflow.com/questions/78119546/how-to-properly-set-build-gradle-kts-encoding) about that.
