plugins {
    id("io.github.gradle-nexus.publish-plugin") version "1.0.0"
}

nexusPublishing {
    repositories {
        sonatype {
            stagingProfileId = "81213a22c3cee5"
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}