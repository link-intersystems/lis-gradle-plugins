plugins {
    id("com.link-intersystems.gradle.multi-module")
}


dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}


dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("${rootDir}/libs.versions.toml"))
        }
    }
}