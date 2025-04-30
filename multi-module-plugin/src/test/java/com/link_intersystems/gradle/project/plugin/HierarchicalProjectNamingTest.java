package com.link_intersystems.gradle.project.plugin;

import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.Condition;
import org.gradle.testkit.runner.BuildResult;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class HierarchicalProjectNamingTest extends AbstractMultiModuleIntegrationTest {

    @Test
    void smokeTest() throws Exception {
        projectBuilder.settingsFile().append(writer -> {
            writer.println("configure<MultiModuleExtension> {");
            writer.println("    projectNamingStrategy = ProjectNamingStrategies.SIMPLE;");
            writer.println("}");
        });
        projectBuilder.createSubproject("moduleA/api");
        projectBuilder.createSubproject("moduleA/impl");
        projectBuilder.createSubproject("moduleB/subB/api");


        BuildResult buildResult = gradleRunner.withArguments("projects", "--info").build();

        AbstractStringAssert<?> outputAssert = assertThat(buildResult.getOutput());

        outputAssert.contains("Including 'moduleA/api' as ':api'");
        outputAssert.contains("Including 'moduleA/impl' as ':impl'");
        outputAssert.contains("Omitting 'moduleB/subB/api'");
    }
}
