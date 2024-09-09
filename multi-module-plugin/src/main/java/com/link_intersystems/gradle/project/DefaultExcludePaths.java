package com.link_intersystems.gradle.project;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class DefaultExcludePaths implements Predicate<Path> {

    private final List<String> excludePaths;

    public DefaultExcludePaths(String... excludePaths) {
        this(Arrays.asList(excludePaths));
    }

    public DefaultExcludePaths(List<String> excludePaths) {
        this.excludePaths = new ArrayList<>(excludePaths);
    }

    @Override
    public boolean test(Path path) {
        return excludePaths.contains(path.toString());
    }
}
