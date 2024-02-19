plugins {
    `maven-publish`
    id("com.link-intersystems.gradle.distribution") version "1.0.0"
}

gradleDist {
    version = "8.5"
}

publishing {
    repositories {
        mavenLocal {
            url = uri(layout.buildDirectory.file("maven-repo"))
        }
    }
}

