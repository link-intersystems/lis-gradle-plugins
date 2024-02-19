pluginManagement {
    repositories {
        maven {
            url = uri(layout.rootDirectory.file("../com.link-intersystems.gradle.distribution/build/maven-repo"))
        }
        mavenCentral()
        gradlePluginPortal()
    }
}