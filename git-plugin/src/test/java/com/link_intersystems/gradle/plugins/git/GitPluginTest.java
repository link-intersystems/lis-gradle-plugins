package com.link_intersystems.gradle.plugins.git;

import com.link_intersystems.git.test.CommitContentBuilder;
import com.link_intersystems.git.test.TestRepository;
import com.link_intersystems.gradle.project.builder.GradleProjectBuilder;
import org.assertj.core.api.AbstractStringAssert;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class GitPluginTest {


    private TestRepository testRepository;
    private GradleProjectBuilder projectBuilder;
    private GradleRunner gradleRunner;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws Exception {
        testRepository = new TestRepository(tempDir);
        projectBuilder = GradleProjectBuilder.rootProject(tempDir);
        projectBuilder.buildFile().append(pw -> {
            pw.println("plugins {");
            pw.println("   id(\"com.link-intersystems.gradle.git\")");
            pw.println("}");
            pw.println();
        });
        projectBuilder.gradleProperties().append(getClass().getResource("/testkit-gradle.properties"));
        testRepository.createCommit("Added git-info plugin.", CommitContentBuilder::addAllFiles);
        gradleRunner = GradleRunner.create().withPluginClasspath().withProjectDir(tempDir.toFile()).forwardOutput();
    }

    @Test
    void gitInfoSmokeTest() throws Exception {
        projectBuilder.buildFile().append(pw -> {
            pw.println("println(\"\"\"");
            pw.println("commit ${gitInfo.commitId} (HEAD -> ${gitInfo.branch})");
            pw.println("Author:     ${gitInfo.authorName} <${gitInfo.authorEmail}>");
            pw.println("AuthorDate: ${gitInfo.authorDateTime}");
            pw.println("Commit:     ${gitInfo.committerName} <${gitInfo.committerEmail}>");
            pw.println("CommitDate: ${gitInfo.commitDateTime}");
            pw.println();
            pw.println("    ${gitInfo.commitMessage}");
            pw.println("\"\"\".trimIndent())");

            pw.println("tasks.create<DefaultTask>(\"listBranches\") {");
            pw.println("    doLast {");
            pw.println("       git.branchList().call()");
            pw.println("   }");
            pw.println("}");
        });

        testRepository.createCommit("Added git-info println.", CommitContentBuilder::addAllFiles);

        BuildResult buildResult = gradleRunner.withArguments("projects").build();
        AbstractStringAssert<?> outputAssert = assertThat(buildResult.getOutput());
        outputAssert.contains("Added git-info println.");
    }

    @Test
    void gitPorcelainApiSmokeTest() throws Exception {
        projectBuilder.buildFile().append(pw -> {
            pw.println("tasks.create<DefaultTask>(\"listBranches\") {");
            pw.println("    doLast {");
            pw.println("      val result = git.branchList().call()");
            pw.println("      result.forEach { ref -> ");
            pw.println("         println(\"Branch: ${ref.name}\")");
            pw.println("      }");
            pw.println("   }");
            pw.println("}");
        });

        testRepository.createCommit("Added listBranches task.", CommitContentBuilder::addAllFiles);

        BuildResult buildResult = gradleRunner.withArguments("listBranches").build();
        AbstractStringAssert<?> outputAssert = assertThat(buildResult.getOutput());
        outputAssert.contains("Branch: refs/heads/main");
    }
}