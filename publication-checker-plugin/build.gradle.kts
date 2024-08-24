plugins {
    id("lis-gradle-plugin")
}

dependencies {
    implementation(slf4j.api)
//    implementation("org.apache.commons:commons-lang3:3.12.0")
//    implementation("org.apache.maven:maven-repository-metadata:3.3.9")


    testImplementation(junit.jupiter.api)
    testImplementation(junit.jupiter.engine)
    testImplementation(mockito.core)


    testImplementation(platform(lis.platform))
    testImplementation(lis.gradleProjectBuilder)
    testImplementation(lis.gradleMocking)
}

gradlePlugin {
    plugins {
        create("publication-checker") {
            id = "com.link-intersystems.gradle.publication-checker"
            implementationClass = "com.link_intersystems.gradle.plugins.publication.PublicationCheckerPlugin"
            displayName = "Publication Checker Plugin"
            description = "Checks if publications can be published to the publishing repositories."
            tags.set(listOf("publication", "publish", "check"))
        }
    }
}

publishing {
    afterEvaluate {
        publications.withType<MavenPublication> {
            pom {
                name.set("Publication Checker Plugin")
                description.set("Checks if publications can be published to the publishing repositories.")
            }
        }
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}