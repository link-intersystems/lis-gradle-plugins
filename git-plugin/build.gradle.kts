plugins {
    id("lis-gradle-plugin")
}

dependencies {
    implementation(slf4j.api)
    implementation(eclipse.jgit)

    testImplementation(junit.jupiter.api)
    testImplementation(junit.jupiter.engine)
    testImplementation(mockito.core)
}

gradlePlugin {
    plugins {
        create("git") {
            id = "com.link-intersystems.gradle.git"
            implementationClass = "com.link_intersystems.gradle.plugins.git.GitPlugin"
        }
    }
}

publishing {
    afterEvaluate {
        publications.withType<MavenPublication> {
            pom {
                name.set("Lis Gradle Git Plugin")
                description.set("Git related extensions and tasks.")
            }
        }
    }
}