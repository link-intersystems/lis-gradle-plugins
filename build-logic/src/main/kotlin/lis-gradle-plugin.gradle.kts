import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    `java-gradle-plugin`
    id("com.gradle.plugin-publish")
    id("lis-gradle-plugin-maven")
    jacoco
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required = true
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacoco/html")
    }
//    executionData.setFrom (fileTree(project.layout.buildDirectory.dir("jacoco")) {
//        include("**/*.exec")
//    })
//
//    doNotTrackState("Don't know why it doesn't work with state.")
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

val libs = the<LibrariesForLibs>()

dependencies {
    jacocoAgentConfig(libs.jacoco.agent)
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