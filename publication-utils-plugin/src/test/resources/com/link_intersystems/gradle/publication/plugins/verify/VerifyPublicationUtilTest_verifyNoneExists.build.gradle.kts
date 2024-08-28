import com.link_intersystems.gradle.publication.VersionProvider
import com.link_intersystems.gradle.publication.plugins.verify.VerifyPublicationResultHandlers
import com.link_intersystems.gradle.publication.plugins.verify.maven.VerifyMavenPublication

plugins {
    id("java-library")
    id("maven-publish")
    id("com.link-intersystems.gradle.publication-utils")
}

group = "com.link-intersystems.gradle.publication.test"
version = "1.0.0-SNAPSHOT"

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

publications {
    verify {
        create<VerifyMavenPublication>("maven") {
            verifyRepositories {
                mavenLocal()
            }
            versionProvider = VersionProvider { _ ->
                "1.0.0"
            }
            resultHandler = VerifyPublicationResultHandlers.NONE_EXISTS
        }

    }
}