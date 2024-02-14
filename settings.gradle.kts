import java.util.function.Predicate

rootProject.name = "lis-gradle-plugins"

pluginManagement {
    includeBuild("convention-plugins")
}

plugins {
    id("lis-gradle-plugins-dependency")
    id("lis-gradle-plugins-multi-module")
}