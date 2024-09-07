import org.apache.tools.ant.filters.ReplaceTokens


plugins {
    `java-gradle-plugin`
    id("com.gradle.plugin-publish")
    id("lis-gradle-plugin-maven")
}


java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }

    withSourcesJar()
    withJavadocJar()
}


tasks.test {
    useJUnitPlatform()
}


gradlePlugin {
    website = "https://github.com/link-intersystems/lis-gradle-plugins"
    vcsUrl = "https://github.com/link-intersystems/lis-gradle-plugins.git"
}


val jacocoAgentConfig by configurations.creating
// Using the string-y api for lookup, because the libs version catalog is not compiled yet.
// See https://discuss.gradle.org/t/using-version-catalog-from-buildsrc-buildlogic-xyz-common-conventions-scripts/48534/22
val libs = the<VersionCatalogsExtension>().named("libs")

dependencies {
    jacocoAgentConfig(libs.findLibrary("jacoco.agent").orElseThrow())
}


tasks.create<Copy>("extractJacoco") {
    from(zipTree(configurations["jacocoAgentConfig"].singleFile)) {
        include("jacocoagent.jar")
    }
    into(project.layout.buildDirectory.dir("jacoco"));
}


tasks.withType<ProcessResources> {
    dependsOn("extractJacoco")
    filesMatching("**/*") {
        expand(project.properties)
        val tokens = mapOf(
            "jacoco_dir" to tasks.getByName("extractJacoco").outputs.files.singleFile.toPath().toString()
                .replace("\\", "/")
        )
        filter<ReplaceTokens>("tokens" to tokens)
    }
}