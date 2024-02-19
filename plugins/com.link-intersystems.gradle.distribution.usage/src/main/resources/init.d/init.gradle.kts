import java.lang.IllegalStateException
import java.net.URI
import java.util.*

apply<EnterpriseRepositoryPlugin>()

val properties = Properties()
val gradlePropertiesFile = File(getGradleUserHomeDir(), "gradle.properties")
if (gradlePropertiesFile.exists()) {
    properties.load(File(getGradleUserHomeDir(), "gradle.properties").inputStream())
}


class EnterpriseRepositoryPlugin : Plugin<Gradle> {
    companion object {
        const val ENTERPRISE_REPOSITORY_NAME = "STANDARD_ENTERPRISE_REPO"
        const val ENTERPRISE_REPOSITORY_URL = "http://localhost:8081/nexus/content/groups/public/"
        const val ENTERPRISE_REPOSITORY_PREFIX = "http://localhost:8081/nexus/"
    }

    override fun apply(gradle: Gradle) {
        // ONLY USE ENTERPRISE REPO FOR DEPENDENCIES
        gradle.allprojects {
            repositories {
                // Remove all repositories not pointing to the enterprise repository url
                all {
                    if (this !is MavenArtifactRepository || !url.toString().startsWith(ENTERPRISE_REPOSITORY_URL, ignoreCase = true)) {
                        val msg = "Repository ${(this as? MavenArtifactRepository)?.url ?: name} is not an organization repo and will be removed. Only repos starting with $ENTERPRISE_REPOSITORY_PREFIX are allowed"
                        project.logger.lifecycle(msg)
                        remove(this)
                    }

                }

                // add the enterprise repository
                add(maven {
                    name = ENTERPRISE_REPOSITORY_NAME
                    url = uri(ENTERPRISE_REPOSITORY_URL)
                    isAllowInsecureProtocol = true

                    if (properties.containsKey("maven.repo.token")) {
                        throw IllegalStateException("maven.repo.token support is not implemented yet. Remove it from ~/gradle.properties")
                    } else {
                        val usernameValue = properties["maven.repo.username"] as String?
                        val passwordValue = properties["maven.repo.password"] as String?

                        if (usernameValue != null) {
                            project.logger.lifecycle("Configure repository access for user ${usernameValue} to ${name} ")

                            credentials {
                                username = usernameValue
                                password = passwordValue
                            }
                        }
                    }
                })

            }
        }
    }
}

data class RepositoryData(val name: String, val url: URI)

allprojects {
    tasks.register("repos") {
        val repositoryData = repositories.withType<MavenArtifactRepository>().map { RepositoryData(it.name, it.url) }
        doLast {
            repositoryData.forEach {
                println("repository: ${it.name} ('${it.url}')")
            }
        }
    }
}
