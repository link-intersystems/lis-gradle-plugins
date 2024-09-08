rootProject.name = "lis-gradle-plugins"


pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("lis-gradle-plugin-settings")
}