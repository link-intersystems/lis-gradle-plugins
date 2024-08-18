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