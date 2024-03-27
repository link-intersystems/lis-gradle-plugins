# Git Plugin

The Git plugin provides extension to Gradle projects so that you can access git information
and use [JGit's procelain api](https://archive.eclipse.org/jgit/docs/jgit-3.2.0.201312181205-r/apidocs/org/eclipse/jgit/api/Git.html). Take a look at examples [here](https://github.com/centic9/jgit-cookbook/tree/master/src/main/java/org/dstadler/jgit/porcelain)

## Plugin Usage

```kotlin
//build.gradle.kts
plugins {
    id("com.link-intersystems.gradle.git") version "0.3.0"
}
```

## Git Info

You can then access the `gitInfo` extension that is available 
in your `build.gralde.kts` in which you applied the plugin. For details
about the `gitInfo` properties take a look at [`GitInfo`](src/main/java/com/link_intersystems/gradle/plugins/git/GitInfo.java) 

```kotlin
println(gitInfo.shortCommitId)

println("""
commit ${gitInfo.commitId} (HEAD -> ${gitInfo.branch})
Author:     ${gitInfo.authorName} <${gitInfo.authorEmail}>
AuthorDate: ${gitInfo.authorDateTime}
Commit:     ${gitInfo.committerName} <${gitInfo.committerEmail}>
CommitDate: ${gitInfo.commitDateTime}

    ${gitInfo.commitMessage}
""".trimIndent())
```

## "GitProcelain"-like API

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