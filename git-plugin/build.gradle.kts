plugins {
    id("lis-gradle-plugin")
}

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.9.0.202403050737-r")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.mockito:mockito-core:5.11.0")
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