package com.link_intersystems.gradle.plugins.git;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;
import java.io.PrintWriter;
import java.io.StringWriter;

import static java.util.Objects.requireNonNull;

class GitInfoTask extends DefaultTask {

    private GitInfo gitInfo;

    @Inject
    public GitInfoTask(GitInfo gitInfo) {
        this.gitInfo = requireNonNull(gitInfo);
    }

    @TaskAction
    public void execute() {
        StringWriter sw = new StringWriter();


        try (PrintWriter info = new PrintWriter(sw)) {
            info.print("commit ");
            info.print(gitInfo.getCommitId());
            info.print(" (HEAD) -> ");
            info.println(gitInfo.getBranch());
            info.println();

            info.print("Author:     ");
            info.print(gitInfo.getAuthorName());
            info.print("<");
            info.print(gitInfo.getAuthorEmail());
            info.println(">");
            info.print("AuthorDate: ");
            info.println(gitInfo.getAuthorDateTime());

            info.print("Commit:     ");
            info.print(gitInfo.getCommitterName());
            info.print("<");
            info.print(gitInfo.getCommitterEmail());
            info.println(">");
            info.print("CommitDate:  ");
            info.println(gitInfo.getCommitDateTime());

            info.println();

            info.println(gitInfo.getCommitMessage());

        }

        getLogger().lifecycle(sw.toString());
    }
}
