package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.initialization.IncludedBuildSpec;

import java.util.List;

public interface IncludeBuildsSource {

    public List<String> getIncludedBuilds();
}
