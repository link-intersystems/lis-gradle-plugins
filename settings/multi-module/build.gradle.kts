plugins {
    `java-gradle-plugin`
    id("maven-publish") // if you never publish the plugin, you may remove this (but it also does not hurt)
}

group = "com.link-intersystems.gradle.plugins"
version = "0.1"

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("commons-io:commons-io:2.15.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.mockito:mockito-core:5.11.0")

}

gradlePlugin {
    plugins {
        create("multi-module-plugin") {
            id = "com.link-intersystems.gradle.multi-module"
            implementationClass = "com.link_intersystems.gradle.plugins.multimodule.MultiModulePlugin"
        }
    }
}

publishing.repositories.maven {
    name = "project"
    url = uri(File(rootDir, "build/.m2/repository"))
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.test {
    useJUnitPlatform()
}