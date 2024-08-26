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
        create("publication-utils") {
            id = "com.link-intersystems.gradle.publication-utils"
            implementationClass = "com.link_intersystems.gradle.plugins.publication.utils.PublicationUtilsPlugin"
            displayName = "Publication Utils Plugin"
            description =
                "A collection of utility tasks to deal with publications related stuff like checking if publications are already published to a remote repository."
            tags.set(listOf("publication", "publish", "check", "util"))
        }
    }
}

publishing {
    afterEvaluate {
        publications.withType<MavenPublication> {
            pom {
                name.set("Publication Utils Plugin")
                description.set("A collection of utility tasks to deal with publications related stuff like checking if publications are already published to a remote repository.")
            }
        }
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}