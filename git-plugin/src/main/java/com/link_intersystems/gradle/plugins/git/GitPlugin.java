package com.link_intersystems.gradle.plugins.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionContainer;

import java.io.File;
import java.io.IOException;

public class GitPlugin implements Plugin<Project> {
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
            extensions.add("gitInfo", new JGitInfo(git));
        } catch (IOException ex) {
            project.getLogger().warn("Project '" + project.getName() + "' located in '" + rootDir + "' doesn't seem to have a git repository. Git extensions will not be available.", ex);
        }
    }


}
