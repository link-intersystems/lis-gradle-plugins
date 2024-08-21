import org.gradle.tooling.GradleConnector

plugins {
    id("com.link-intersystems.gradle.maven-central-project") version "0.0.5"
    id("net.researchgate.release") version "3.0.2"

}


// Work around for Cannot include build 'build-logic' in build '???'. This is not supported yet.
// See https://github.com/researchgate/gradle-release/issues/304
configure(listOf(tasks.release, tasks.runBuildTasks)) {
    configure {
        actions.clear()
        doLast {
            org.gradle.tooling.GradleConnector
                .newConnector()
                .forProjectDirectory(layout.projectDirectory.asFile)
                .connect()
                .use { projectConnection ->
                    val buildLauncher = projectConnection
                        .newBuild()
                        .forTasks(*tasks.toTypedArray())
                        .setStandardInput(java.lang.System.`in`)
                        .setStandardOutput(java.lang.System.out)
                        .setStandardError(java.lang.System.err)
                    gradle.startParameter.excludedTaskNames.forEach {
                        buildLauncher.addArguments("-x", it)
                    }
                    gradle.startParameter.projectProperties.forEach { t, u ->
                        buildLauncher.addArguments("-P$t=$u")
                    }

                    buildLauncher.run()
                }
        }
    }
}

val pushToRemoteName = if(project.findProperty("pushToRemote") != null) "origin" else ""

release {
    tagTemplate = "v\${version}"

    git {
        pushToRemote = pushToRemoteName
    }
}