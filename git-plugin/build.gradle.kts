plugins {
    id("lis-gradle-plugin")
}

dependencies {
    implementation(slf4j.api)
    implementation(eclipse.jgit)

    testImplementation(junit.jupiter.api)
    testImplementation(junit.jupiter.engine)
    testImplementation(mockito.core)
    testImplementation(assertj.core)

    testImplementation(platform(lis.platform))
    testImplementation(lis.gradleProjectBuilder)
}

gradlePlugin {
    plugins {
        create("git") {
            id = "com.link-intersystems.gradle.git"
            implementationClass = "com.link_intersystems.gradle.plugins.git.GitPlugin"
            displayName = "Gradle Git Plugin"
            description = "Access Git information and execute Git actions from within a Gradle build."
            tags.set(listOf("git"))
        }
    }
}

publishing {
    afterEvaluate {
        publications.withType<MavenPublication> {
            pom {
                name.set("Gradle Git Plugin")
                description.set("Access Git information and execute Git actions from within a Gradle build.")
            }
        }
    }
}