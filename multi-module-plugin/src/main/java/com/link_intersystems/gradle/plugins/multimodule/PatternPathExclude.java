package com.link_intersystems.gradle.plugins.multimodule;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

class PatternPathExclude implements Predicate<Path> {

    private final List<String> appliedPaths = new ArrayList<>();
    private final List<String> ignoredPaths = new ArrayList<>();
    private List<String> excludePaths;
    private List<PathMatcher> excludedPaths;

    public PatternPathExclude() {
        excludePaths = new ArrayList<>();
    }

    public PatternPathExclude(List<String> excludePaths) {
        this.excludePaths = excludePaths;
    }


    public void setExcludePaths(List<String> excludePaths) {
        this.excludePaths = new ArrayList<>(excludePaths);
        excludedPaths = null;
    }

    private void ensureInitialized() {
        if (excludedPaths == null) {
            excludedPaths = new ArrayList<>();
            FileSystem fileSystem = FileSystems.getDefault();
            for (String excludePath : excludePaths) {
                try {
                    if (!excludePath.startsWith("regex:") && !excludePath.startsWith("glob:")) {
                        excludePath = "glob:" + excludePath;
                    }
                    PathMatcher pathMatcher = fileSystem.getPathMatcher(excludePath);
                    excludedPaths.add(pathMatcher);
                    appliedPaths.add(excludePath);
                } catch (Exception e) {
                    ignoredPaths.add(excludePath);
                }
            }
        }
    }

    public List<String> getAppliedPaths() {
        ensureInitialized();
        return appliedPaths;
    }

    public List<String> getIgnoredPaths() {
        ensureInitialized();
        return ignoredPaths;
    }

    @Override
    public boolean test(Path path) {
        ensureInitialized();
        for (PathMatcher excludedPath : excludedPaths) {
            if (excludedPath.matches(path)) {
                return true;
            }
        }
        return false;
    }
}
