plugins {
    id("lis-gradle-plugin")
}

dependencies {
    implementation(slf4j.api)

    testImplementation(junit.jupiter.api)
    testImplementation(junit.jupiter.engine)
    testImplementation(mockito.core)


    testImplementation(platform(lis.platform))
    testImplementation(lis.gradleProjectBuilder)
    testImplementation(lis.gradleMocking)
}

gradlePlugin {
    plugins {
        create("publishing-check") {
            id = "com.link-intersystems.gradle.publishing-check"
            implementationClass = "com.link_intersystems.gradle.plugins.multimodule.MultiModulePlugin"
            displayName = "Publishing Checker Plugin"
            description = "Checks if publications can be published to the publishing repositories."
            tags.set(listOf("publishing", "publish", "check"))
        }
    }
}

publishing {
    afterEvaluate {
        publications.withType<MavenPublication> {
            pom {
                name.set("Publishing Check Plugin")
                description.set("Checks if publications can be published to the publishing repositories.")
            }
        }
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}