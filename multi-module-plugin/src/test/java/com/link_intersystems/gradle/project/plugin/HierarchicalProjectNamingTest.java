package com.link_intersystems.gradle.project.plugin;

import com.link_intersystems.gradle.project.builder.FileBuilder;
import com.link_intersystems.gradle.project.builder.GradleSubprojectBuilder;
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
        GradleSubprojectBuilder moduleBImpl = projectBuilder.createSubproject("moduleB/impl");
        FileBuilder modulePropertiesBuilder = moduleBImpl.file("module.properties");
        modulePropertiesBuilder.append(writer -> writer.println("name = moduleB-impl"));
        projectBuilder.createSubproject("moduleB/subB/api");


        BuildResult buildResult = gradleRunner.withArguments("projects", "--info", "--stacktrace").build();

        AbstractStringAssert<?> outputAssert = assertThat(buildResult.getOutput());

        outputAssert.contains("Including 'moduleA/api' as ':api'");
        outputAssert.contains("Including 'moduleA/impl' as ':impl'");
        outputAssert.contains("Including 'moduleB/impl' as ':moduleB-impl'");
        outputAssert.contains("Omitting 'moduleB/subB/api'");
    }
}
