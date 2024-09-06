plugins {
    id("lis-gradle-plugin")
}

dependencies {
    implementation(slf4j.api)

    testImplementation(junit.jupiter.api)
    testImplementation(junit.jupiter.engine)
    testImplementation(mockito.core)
    testImplementation(gradleTestKit())
    testImplementation("org.assertj:assertj-core:3.26.3")



    testImplementation(platform(lis.platform))
    testImplementation(lis.gradleProjectBuilder)
    testImplementation(lis.gradleMocking)
}

gradlePlugin {
    plugins {
        create("multi-module") {
            id = "com.link-intersystems.gradle.multi-module"
            implementationClass = "com.link_intersystems.gradle.plugins.multimodule.MultiModulePlugin"
            displayName = "Multi Module Plugin"
            description = "Configures the Gradle build by automatically detecting subprojects and composite builds."
            tags.set(listOf("multi-module", "composite-builds", "sub-projects"))
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