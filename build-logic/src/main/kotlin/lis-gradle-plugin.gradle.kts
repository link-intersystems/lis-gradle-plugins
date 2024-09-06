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

val jacocoAgentConfig by configurations.creating {
}

dependencies {
    jacocoAgentConfig("org.jacoco:org.jacoco.agent:0.8.12")
}


tasks.create<Copy>("copyJacoco") {
    from(zipTree(configurations["jacocoAgentConfig"].singleFile)) {
        include("jacocoagent.jar")
    }
    into(project.layout.buildDirectory.dir("jacoco"));
}

tasks.withType<ProcessResources> {
    dependsOn("copyJacoco")
    filesMatching("**/*") {
        expand(project.properties)
        val tokens = mapOf(
            "jacoco_dir" to tasks.getByName("copyJacoco").outputs.files.singleFile.toPath().toString()
                .replace("\\", "/")
        )
        filter<ReplaceTokens>("tokens" to tokens)
    }
}