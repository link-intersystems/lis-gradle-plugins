package com.link_intersystems.gradle.plugins.multimodule;

import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.Condition;
import org.gradle.testkit.runner.BuildResult;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiModulePluginIntegrationTest extends AbstractMultiModuleIntegrationTest {

    @Test
    void smokeTest() throws Exception {
        projectBuilder.createCompositeBuild("buildSrc");
        projectBuilder.createCompositeBuild("modules/compositeA").createSubproject("subA");
        projectBuilder.createSubproject("modules/moduleA");
        projectBuilder.createSubproject("modules/moduleB");
        projectBuilder.createSubproject("modules/moduleB/moduleC");
        projectBuilder.createSubproject("modules/moduleB/moduleC/moduleD");
        projectBuilder.createCompositeBuild("modules/.hiddenModuleA");

        projectBuilder.buildFile().append(pw -> {
            pw.println("project.layout.projectDirectory.dir(\".moduleMarker\").asFile.mkdirs()");
            pw.println("subprojects.forEach {");
            pw.println("    File(project.layout.projectDirectory.dir(\".moduleMarker\").asFile, it.name).createNewFile()");
            pw.println("}");
        });

        projectBuilder.settingsFile().append(pw -> {
            pw.println("configure<MultiModuleConfig> {");
            pw.println("    excludedPaths = listOf(\"**/*C\")");
            pw.println("}");
        });

        BuildResult buildResult = gradleRunner.withArguments("projects").build();

        Path path = projectBuilder.getPath();
        Path moduleMarkers = path.resolve(".moduleMarker");

        assertThat(moduleMarkers.resolve("modules")).exists();
        assertThat(moduleMarkers.resolve("moduleA")).exists();
        assertThat(moduleMarkers.resolve("moduleB")).exists();

        assertThat(moduleMarkers).isDirectory().satisfies(new Condition<>(p -> {
            try (Stream<Path> files = Files.list(p)) {
                return files.count() == 3;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, "File count"));

        AbstractStringAssert<?> outputAssert = assertThat(buildResult.getOutput());

        outputAssert.contains("\\--- Included build ':compositeA'");
    }
}
