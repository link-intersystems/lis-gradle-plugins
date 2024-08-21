plugins {
    `java-gradle-plugin`
    id("lis-gradle-plugins-maven-central-artifact")
    id("com.link-intersystems.gradle.maven-central-library")
    id("com.gradle.plugin-publish")
}

gradlePlugin {
    website = "https://github.com/link-intersystems/lis-gradle-plugins"
    vcsUrl = "https://github.com/link-intersystems/lis-gradle-plugins.git"
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
}

// Work around to allow the git-publish-plugin to coexist with the signing configuration provided by lis-gradle-plugins-maven-central-artifact
// Otherwise we will get an error:
//    * What went wrong:
//    Some problems were found with the configuration of task ':git-plugin:signPluginMavenPublication' (type 'Sign').
//    - Gradle detected a problem with the following location: 'D:\repos\git\github\link-intersystems\lis-gradle-plugins\git-plugin\build\libs\git-plugin-0.5.2-SNAPSHOT.jar.asc'.
//
//    Reason: Task ':git-plugin:publishMavenPublicationToSonatypeRepository' uses this output of task ':git-plugin:signPluginMavenPublication' without declaring an explicit or implicit dependency. This can lead to incorrect results being produced, depending on what order the tasks are executed.
//
//    Possible solutions:
//    1. Declare task ':git-plugin:signPluginMavenPublication' as an input of ':git-plugin:publishMavenPublicationToSonatypeRepository'.
//    2. Declare an explicit dependency on ':git-plugin:signPluginMavenPublication' from ':git-plugin:publishMavenPublicationToSonatypeRepository' using Task#dependsOn.
//    3. Declare an explicit dependency on ':git-plugin:signPluginMavenPublication' from ':git-plugin:publishMavenPublicationToSonatypeRepository' using Task#mustRunAfter.
//
//    For more information, please refer to https://docs.gradle.org/8.8/userguide/validation_problems.html#implicit_dependency in the Gradle documentation.
//
// This error happens, because the PublishToMavenRepository tasks do not depend on the Sign tasks configured by the git-plugin.
// So we just configure them here
afterEvaluate {
    tasks.withType<Sign> sign@{
        tasks.withType<PublishToMavenRepository> {
            dependsOn(this@sign)
        }
    }
}