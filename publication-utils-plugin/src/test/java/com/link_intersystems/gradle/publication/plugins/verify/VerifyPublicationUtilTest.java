package com.link_intersystems.gradle.publication.plugins.verify;

import com.link_intersystems.gradle.junit.GradleIntegrationTest;
import com.link_intersystems.gradle.project.builder.FileBuilder;
import com.link_intersystems.gradle.project.builder.GradleProjectBuilder;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;

@GradleIntegrationTest
class VerifyPublicationUtilTest {

    private GradleProjectBuilder rootProjectBuilder;
    private GradleRunner gradleRunner;

    @BeforeEach
    void setUp(GradleProjectBuilder rootProjectBuilder, GradleRunner gradleRunner) {
        this.rootProjectBuilder = rootProjectBuilder;
        this.gradleRunner = gradleRunner;
    }

    @Test
    void verifyNoneExists() {
        FileBuilder rootBuildScript = rootProjectBuilder.buildFile();
        fromResource(rootBuildScript, "verifyNoneExists.build.gradle.kts");

        gradleRunner.withArguments(":verifyMavenPublicationToMavenLocalRepository").build();
    }

    @Test
    void verifyNoneExistsFailed() throws IOException {
        FileBuilder rootBuildScript = rootProjectBuilder.buildFile();
        rootProjectBuilder.settingsFile().append("rootProject.name = \"test-artifact\"");
        fromResource(rootBuildScript, "verifyNoneExistsFailed.build.gradle.kts");
        FileBuilder pomContent = rootProjectBuilder.file(".m2/repository/com/link-intersystems/test/test-artifact/1.2.3/test-artifact-1.2.3.pom");
        pomContent.append("");

        gradleRunner.withArguments(":verifyMavenPublicationToTempLocalRepository").buildAndFail();
    }

    private void fromResource(FileBuilder fileBuilder, String resource) {
        fileBuilder.append(Objects.requireNonNull(getClass().getResource(getClass().getSimpleName() + "_" + resource)));
    }

}