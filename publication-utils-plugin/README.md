# Publication Utils Plugin [![Gradle Plugin Portal Version](https://img.shields.io/gradle-plugin-portal/v/com.link-intersystems.gradle.publication-utils)](https://plugins.gradle.org/plugin/com.link-intersystems.gradle.publication-utils) [![Maven Central Version](https://img.shields.io/maven-central/v/com.link-intersystems.gradle.publication-utils/com.link-intersystems.gradle.publication-utils.gradle.plugin)](https://mvnrepository.com/artifactCoordinates/com.link-intersystems.gradle.publication-utils)

> [!NOTE]
> Currently only Maven publications are supported.

## Verify Publications

The verify publication utils provide support for verifying the state of publication artifacts in remote repositories.

The following configuration will create a `VerifyPublicationTask` for the publication named "maven".
The task will verify if the publication artifacts exist in all remote repositories that were configured for the
publication.

```kotlin
// build.gradle.kts
publications {
    verify {
        create<VerifyMavenPublication>("maven") {
        }
    }
}
```

The task will fail if one artifact exist of the publication `maven` exists any remote repository. But there are several
options to change this behaviour.
Read more about the configuration options in the sections below.

The tasks will be added to the publications group.

```shell
$ ./gradlew tasks

Publications tasks
------------------
verifyMavenPublicationToMavenRepoRepository
```

### Specify other artifacts

You can also create verify publication tasks that check arbitrary artifacts.

```kotlin
// build.gradle.kts
publications {
    verify {
        create<VerifyMavenPublication>("maven") {
            artifacts = listOf("org.junit.jupiter:junit-jupiter-api:5.10.2:pom")
        }
    }
}
```

The artifact coordinates depend on the verify publication task type.

| Verify Publication Type | Format                                                        |
|-------------------------|---------------------------------------------------------------|
| VerifyMavenPublication  | `<groupId>:<artifactId>:<version>:<extension>[:<classifier>]` |

> [!NOTE]  
> When artifacts are explicitly specified the task will not resolve the project publication artifacts anymore.
> If you also want to resolve them, you need to create another task.

### Specify other repositories

Usually the `VerifyPublicationTask` will use all remote repositories that you configured for the publishing. E.g.

```kotlin
publishing {
    repositories {
        maven {
            val releasesRepoUrl = layout.buildDirectory.dir("repos/releases")
            val snapshotsRepoUrl = layout.buildDirectory.dir("repos/snapshots")
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
        }
    }
}
```

But you can specify custom repositories if needed. E.g. check if the artifacts exists in the maven central repository.

```kotlin
publications {
    verify {
        create<VerifyMavenPublication>("maven") {
            verifyRepositories {
                mavenCentral()
            }
        }
    }
}
```

`verifyRepositories` is a [
`MavenVerifyRepositoryHandler`](src/main/java/com/link_intersystems/gradle/publication/plugins/verify/maven/MavenVerifyRepositoryHandler.java),
the api can be used the same way as the [
`RepositoryHandler`](https://docs.gradle.org/current/javadoc/org/gradle/api/artifacts/dsl/RepositoryHandler.html) that
you
might already know from the [publishing plugin](https://docs.gradle.org/current/userguide/publishing_maven.html),
except that the types of repositories you can create depend on the verify task type you use. In the example above only
maven repositories can be created, because the `VerifyMavenPublication` task type is used.

### Version provider

Sometimes you don't want to check the current project artifacts version. Instead, you want to verify another version.
For this purpose you can specify a [
`VersionProvider`](src/main/java/com/link_intersystems/gradle/publication/VersionProvider.java). E.g. if you want to
check if the release version of the current
snapshot version already exists.

```kotlin
import com.link_intersystems.gradle.publication.VersionProviders

publications {
    verify {
        create<VerifyMavenPublication>("maven") {
            versionProvider = VersionProviders.RELEASE_VERSION
        }
    }
}
```

But feel free to implement your own [
`VersionProvider`](src/main/java/com/link_intersystems/gradle/publication/VersionProvider.java):

```kotlin
publications {
    verify {
        create<VerifyMavenPublication>("maven") {
            // A fixed version provider
            versionProvider = VersionProvider { _ -> // parameter unused. Rename to use it
                "1.0.0"
            }
        }
    }
}
```

The parameter is of type [`ArtifactDesc`](src/main/java/com/link_intersystems/gradle/publication/ArtifactDesc.java)

### Verify result handling

The default result handler throws an exception and breaks the build if the artifacts already exist. But you can
change this behaviour. Either you use one of the pre-defined result handlers in `VerifyPublicationResultHandlers` or
you implement your own.

```kotlin
import com.link_intersystems.gradle.publication.plugins.verify.VerifyPublicationResultHandlers

publications {
    verify {
        create<VerifyMavenPublication>("maven") {
            resultHandler = VerifyPublicationResultHandlers.REPORT_ONLY
            // resultHandler = VerifyPublicationResultHandlers.NONE_EXISTS // the default
            // resultHandler = VerifyPublicationResultHandlers.ALL_EXIST
        }
    }
}
```

### Artifact filters

An `ArtifactFilter` can be used if you don't want to check all artifacts that belong to a publication. The
default `ArtifactFilter` will accept all artifacts.

```kotlin
publications {
    verify {
        create<VerifyMavenPublication>("maven") {
            artifactFilter = ArtifactFilter { coords ->
                coords.extension == "pom"
            }
        }
    }
}
```

The parameter depends on the verify publication task type


| Verify Publication Type | Parameter                             |
|-------------------------|---------------------------------------|
| VerifyMavenPublication  | [MavenArtifactCoordinates](src/main/java/com/link_intersystems/gradle/publication/maven/MavenArtifactCoordinates.java) |


### Git-flow support

When you are using the git-flow branching model you have release branches to prepare the next release and a main
(production) branch that only contains merge commits of the release branch.

If you want to achieve a full continuous delivery pipeline you also want to automatically deploy on repository events,
like a merge into the main branch or a version tag creation. Thus, you will define pipelines that will run on those
events and publish your publications. It's annoying when these pipelines fail, e.g. because an artifact is
already published, because you forgot to increment the version.

The `VerifyPublicationTask` can help you with these issues, because it checks if your publications might be deployable.
So you configure the `VerifyPublicationTask` to run on the release branch and fail if any release artifact exists.
Because this means that if it passes, you can be sure that your publications can be published.

Here is an example configuration that can achieve this.

```kotlin
publications {
    verify {
        create<VerifyMavenPublication>("maven") {
            versionProvider = VersionProviders.RELEASE_VERSION
        }
    }
}
```