import org.gradle.plugin.management.internal.PluginManagementSpecInternal

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

val nonHiddenDirectoryFilter: (File) -> Boolean = { file -> file.isDirectory && !file.isHidden }
val gradleProjectModuleDir: (File) -> Boolean = { file ->
    (File(file, "build.gradle").exists() ||
            File(file, "build.gradle.kts").exists())
}

val buildSrcDir: (File) -> Boolean = { file ->
    file.name.equals("buildSrc")
}

val rootDirRelativePath: (File) -> String = {
    rootDir.toURI().relativize(it.toURI()).toString().dropLastWhile { c ->
        c == '/'
    }
}

val relativeModulePath: (String) -> String = {
    it.replace("/", ":")
}

val isIndependentProject: (File) -> Boolean = { dir ->
    File(dir, "settings.gradle").exists() || File(dir, "settings.gradle.kts").exists()
}

val includedSettingsPluginDirs = (settings.pluginManagement as PluginManagementSpecInternal).includedBuilds.map { it.rootDir }.toList()
val isConventionPluginDir: (File) -> Boolean = {
    includedSettingsPluginDirs.contains(it)
}

rootDir.walk(FileWalkDirection.TOP_DOWN)
        .filter(nonHiddenDirectoryFilter)
        .filterNot(buildSrcDir)
        .filterNot(isConventionPluginDir)
        .filter(gradleProjectModuleDir)
        .filterNot(isIndependentProject)
        .map(rootDirRelativePath)
        .map(relativeModulePath)
        .forEach {
            include(it)
        }

rootDir.walk(FileWalkDirection.TOP_DOWN)
        .filter(nonHiddenDirectoryFilter)
        .filterNot(buildSrcDir)
        .filterNot(isConventionPluginDir)
        .filter(gradleProjectModuleDir)
        .filter(isIndependentProject)
        .map(rootDirRelativePath)
        .forEach {
            includeBuild(it)
        }