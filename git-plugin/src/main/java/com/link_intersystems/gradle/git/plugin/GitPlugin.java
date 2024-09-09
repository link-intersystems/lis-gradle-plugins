package com.link_intersystems.gradle.git.plugin;

import com.link_intersystems.gradle.git.GitInfoTask;
import com.link_intersystems.gradle.git.jgit.JGitInfo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.tasks.TaskProvider;

import java.io.File;
import java.io.IOException;

class GitPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        File rootDir = project.getRootDir();
        FileRepositoryBuilder fileRepositoryBuilder = new FileRepositoryBuilder();

        fileRepositoryBuilder.readEnvironment();
        fileRepositoryBuilder.findGitDir(rootDir);

        try {
            Repository repository = fileRepositoryBuilder.build();
            ExtensionContainer extensions = project.getExtensions();
            Git git = extensions.create("git", Git.class, repository);
            JGitInfo gitInfo = new JGitInfo(git);
            extensions.add("gitInfo", gitInfo);

            TaskProvider<GitInfoTask> gitInfoTask = project.getTasks().register("git-info", GitInfoTask.class, gitInfo);
            gitInfoTask.configure(task -> {
                task.setGroup("git");
                task.setDescription("Outputs information about the actual commit (HEAD).");
            });
        } catch (IOException ex) {
            project.getLogger().warn("Project '" + project.getName() + "' located in '" + rootDir + "' doesn't seem to have a git repository. Git extensions will not be available.", ex);
        }
    }


}
