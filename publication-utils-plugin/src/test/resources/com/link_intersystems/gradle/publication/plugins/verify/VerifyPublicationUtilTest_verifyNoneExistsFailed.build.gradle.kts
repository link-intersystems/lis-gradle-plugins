import com.link_intersystems.gradle.publication.VersionProvider
import com.link_intersystems.gradle.publication.plugins.verify.VerifyPublicationResultHandlers
import com.link_intersystems.gradle.publication.plugins.verify.maven.VerifyMavenPublication

plugins {
    id("java-library")
    id("maven-publish")
    id("com.link-intersystems.gradle.publication-utils")
}

group = "com.link-intersystems.test"
version = "1.2.3"

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "TempLocal"
            url = uri(layout.projectDirectory.dir(".m2/repository"))
        }
    }
}

publications {
    verify {
        create<VerifyMavenPublication>("maven") {
            verifyRepositories {
                maven {
                    name = "TempLocal"
                    url = uri(layout.projectDirectory.dir(".m2/repository"))
                }
            }
            resultHandler = VerifyPublicationResultHandlers.NONE_EXISTS
        }
    }
}