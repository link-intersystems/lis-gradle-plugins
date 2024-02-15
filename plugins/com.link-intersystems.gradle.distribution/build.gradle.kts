plugins {
    `java-gradle-plugin`
    `maven-publish`
    id("com.gradle.plugin-publish") version "1.2.1"
}


group = "com.link-intersystems.gradle"
version = "1.0.0"

dependencies {
    api(gradleApi())
}


gradlePlugin {
    plugins {
        create("com.link-intersystems.gradle.distribution") {
            id = "com.link-intersystems.gradle.distribution"
            displayName = "com.link-intersystems.gradle.distribution"
            description = "Create a custom gradle distribution for your organization or team, that can also be used by gradle wrapper."
            tags = listOf("custom", "wrapper", "init", "initialization", "script")
            implementationClass = "com.link_intersystems.gradle.distribution.plugin.GradleDistributionPlugin"
        }
    }

    website = "https://github.com/link-intersystems/lis-gradle-plugins"
    vcsUrl = "https://github.com/link-intersystems/lis-gradle-plugins"
}

publishing {
    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("maven-repo"))
        }
    }
}