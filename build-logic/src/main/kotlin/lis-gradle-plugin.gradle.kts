plugins {
    `java-gradle-plugin`
    id("com.gradle.plugin-publish")
    id("lis-gradle-plugin-maven")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }

    withSourcesJar()
    withJavadocJar()
}

tasks.withType<ProcessResources> {
    filesMatching("**/*") {
        expand(project.properties)
    }
}

tasks.test {
    useJUnitPlatform()
}

gradlePlugin {
    website = "https://github.com/link-intersystems/lis-gradle-plugins"
    vcsUrl = "https://github.com/link-intersystems/lis-gradle-plugins.git"
}