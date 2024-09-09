package com.link_intersystems.gradle.project.plugin;

import com.link_intersystems.gradle.project.builder.GradleProjectBuilder;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

public class AbstractMultiModuleIntegrationTest {

    protected GradleProjectBuilder projectBuilder;
    protected GradleRunner gradleRunner;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws Exception {
        projectBuilder = GradleProjectBuilder.rootProject(tempDir);
        projectBuilder.settingsFile().append(pw -> {
            pw.println("import com.link_intersystems.gradle.project.plugin.MultiModuleExtension");
            pw.println();
            pw.println("rootProject.name = \"" + getClass().getSimpleName() + "\"");
            pw.println("plugins {");
            pw.println("   id(\"com.link-intersystems.gradle.multi-module\")");
            pw.println("}");
            pw.println();
        });
        projectBuilder.gradleProperties().append(getClass().getResource("/testkit-gradle.properties"));

        gradleRunner = GradleRunner.create().forwardOutput().withProjectDir(tempDir.toFile()).withPluginClasspath();
    }
}
