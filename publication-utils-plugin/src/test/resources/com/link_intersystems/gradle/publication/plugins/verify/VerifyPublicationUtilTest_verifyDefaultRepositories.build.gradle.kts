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
            resultHandler = VerifyPublicationResultHandlers.NONE_EXISTS
        }
    }
}