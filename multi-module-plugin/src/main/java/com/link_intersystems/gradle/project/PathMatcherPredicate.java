package com.link_intersystems.gradle.project;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class PathMatcherPredicate implements Predicate<Path> {

    private final List<PathMatcher> excludedPaths = new ArrayList<>();
    private final List<String> appliedPaths = new ArrayList<>();
    private final List<String> ignoredPaths = new ArrayList<>();

    public PathMatcherPredicate(List<String> excludePaths) {
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

    public List<String> getAppliedPaths() {
        return appliedPaths;
    }

    public List<String> getIgnoredPaths() {
        return ignoredPaths;
    }

    @Override
    public boolean test(Path path) {
        for (PathMatcher excludedPath : excludedPaths) {
            if (excludedPath.matches(path)) {
                return true;
            }
        }
        return false;
    }
}
