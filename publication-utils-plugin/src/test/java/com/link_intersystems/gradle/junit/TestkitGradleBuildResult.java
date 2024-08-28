package com.link_intersystems.gradle.junit;


import static java.util.Objects.requireNonNull;

public class TestkitGradleBuildResult implements GradleBuildResult {
    private final org.gradle.testkit.runner.BuildResult build;

    public TestkitGradleBuildResult(org.gradle.testkit.runner.BuildResult build) {
        this.build = requireNonNull(build);
    }

    @Override
    public String getOutput() {
        return build.getOutput();
    }
}
