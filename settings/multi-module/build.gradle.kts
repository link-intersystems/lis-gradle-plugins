plugins {
    `java-gradle-plugin`
    id("maven-publish") // if you never publish the plugin, you may remove this (but it also does not hurt)
    signing
}

val multiModuleId: String by project

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("commons-io:commons-io:2.15.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.mockito:mockito-core:5.11.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

tasks.withType<ProcessResources> {
    filesMatching("**/*") {
        expand(project.properties)
    }
}

gradlePlugin {
    plugins {
        create("multi-module") {
            id = multiModuleId
            implementationClass = "com.link_intersystems.gradle.plugins.multimodule.MultiModulePlugin"
        }
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}


tasks.test {
    useJUnitPlatform()
}


publishing {
    afterEvaluate {
        publications.withType<MavenPublication> {
            logger.info("MavenPublication: " + this.name)
            pom {
                name.set(multiModuleId)
                description.set("Gradle submodule and composite build support.")
                url.set("https://github.com/link-intersystems/lis-gradle-plugins")
                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("rene.link")
                        name.set("René Link")
                        email.set("rene.link@link-intersystems.com")
                        organization.set("Link Intersystems GmbH")
                        organizationUrl.set("https://www.link-intersystems.com")
                        url.set("https://stackoverflow.com/users/974186/ren%C3%A9-link")
                        roles.set(listOf("developer"))
                    }
                }
                scm {
                    url.set("https://github.com/link-intersystems/lis-gradle-plugins")
                    connection.set("scm:git:https://github.com/link-intersystems/lis-gradle-plugins.git")
                    developerConnection.set("scm:git:https://github.com/link-intersystems/lis-gradle-plugins.git")
                }
            }
        }


    }
}

signing {
    afterEvaluate {
        val signingKey = providers.environmentVariable("GPG_SIGNING_KEY")
        val signingPassphrase = providers.environmentVariable("GPG_SIGNING_PASSPHRASE")
        if (signingKey.isPresent && signingPassphrase.isPresent) {
            useInMemoryPgpKeys(signingKey.get(), signingPassphrase.get())
            sign(publishing.publications)
            logger.lifecycle("Signing publications")
        }
    }
}




