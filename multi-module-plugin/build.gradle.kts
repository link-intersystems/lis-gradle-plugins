plugins {
    id("lis-gradle-plugin")
}

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.12")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.mockito:mockito-core:5.11.0")

    testImplementation("com.link-intersystems.gradle.commons:lis-gradle-project-builder:0.0.2")
    testImplementation("com.link-intersystems.gradle.commons:lis-gradle-mocking:0.0.3")
}

gradlePlugin {
    plugins {
        create("multi-module") {
            id = "com.link-intersystems.gradle.multi-module"
            implementationClass = "com.link_intersystems.gradle.plugins.multimodule.MultiModulePlugin"
        }
    }
}

publishing {
    afterEvaluate {
        publications.withType<MavenPublication> {
            pom {
                name.set("Lis Gradle Multi Module Plugin")
                description.set("Configures the Gradle build by automatically detecting subprojects and composite builds.")
            }
        }
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}