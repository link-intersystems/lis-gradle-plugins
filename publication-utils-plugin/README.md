# Publishing Check Plugin [![Maven Central Version](https://img.shields.io/maven-central/v/com.link-intersystems.gradle.publishing-check/com.link-intersystems.gradle.publishing-check.gradle.plugin)](https://mvnrepository.com/artifactCoordinates/com.link-intersystems.gradle.publishing-check)

Checks if publications can be published to the publishing repositories. 

When you are using the git-flow branching model you have release branches to prepare the next release and a main
(production) branch that only contains merge commits of the release branch.

If you want to achieve a full continuous delivery pipeline you also want to automatically deploy on repository events,
like a merge into the main branch or a version tag creation. Thus, you will define pipelines that will run on those
events and publish your publications. It's annoying when these pipelines fail, e.g. because an artifactCoordinates is already
published or you forgot to increment the version. 

The publishing check plugin can help you with these issues, because it checks if your publications might be deployable.
So if you run this publishing check on your release branch and the build passed, you can be sure that your publications
can be published.