plugins {
    `java-gradle-plugin`
    id("maven-publish") // if you never publish the plugin, you may remove this (but it also does not hurt)
    signing
}

val multiModuleId: String by project

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }

    withSourcesJar()
    withJavadocJar()
}

tasks.withType<ProcessResources> {
    filesMatching("**/*") {
        expand(project.properties)
    }
}

tasks.test {
    useJUnitPlatform()
}

afterEvaluate {
    publishing {
        publications.withType<MavenPublication> {
            pom {
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

    signing {
        val signingKey = providers.environmentVariable("GPG_SIGNING_KEY")
        val signingPassphrase = providers.environmentVariable("GPG_SIGNING_PASSPHRASE")
        if (signingKey.isPresent && signingPassphrase.isPresent) {
            useInMemoryPgpKeys(signingKey.get(), signingPassphrase.get())
            sign(publishing.publications)
            logger.lifecycle("Signing publications")
        }
    }
}









