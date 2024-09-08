import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("lis-gradle-plugin")
}


dependencies {
    implementation(libs.slf4j.api)
    implementation(libs.eclipse.jgit)

    testImplementation(libs.bundles.testing)
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


publishing.publications.withType<MavenPublication> {
    pom {
        name.set("Gradle Git Plugin")
        description.set("Access Git information and execute Git actions from within a Gradle build.")
    }
}