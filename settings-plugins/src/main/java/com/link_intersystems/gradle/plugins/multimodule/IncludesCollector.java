package com.link_intersystems.gradle.plugins.multimodule;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public class IncludesCollector implements FileVisitor<Path> {

    private List<String> includeBuildPaths = new ArrayList<>();
    private List<String> includePaths = new ArrayList<>();

    private Predicate<Path> settingsFilePredicate = new GradleSettingsPredicate();
    private Predicate<Path> buildFilePredicate = new GradleBuildFilePredicate();

    private Path rootPath;
    private Predicate<Path> excludePaths = p -> true;

    public IncludesCollector(Path rootPath) {
        this.rootPath = requireNonNull(rootPath);
    }

    public void setExcludePaths(Predicate<Path> excludePaths) {
        this.excludePaths = requireNonNull(excludePaths);
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        if (dir.equals(rootPath)) {
            return FileVisitResult.CONTINUE;
        }

        if (skipDir(dir)) {
            return FileVisitResult.SKIP_SUBTREE;
        }

        if (isCompositeBuildDir(dir)) {
            includeBuildPaths.add(toIncludeBuildPath(dir));
            return FileVisitResult.SKIP_SUBTREE;
        }

        if (isSubmoduleDir(dir)) {
            includePaths.add(toProjectPath(dir));
            return FileVisitResult.CONTINUE;
        }

        return FileVisitResult.CONTINUE;
    }

    private boolean isSubmoduleDir(Path dir) {
        return buildFilePredicate.test(dir);
    }

    private boolean isCompositeBuildDir(Path dir) {
        return settingsFilePredicate.test(dir);
    }

    private boolean skipDir(Path dir) {
        return isHidden(dir) || isExcluded(dir);
    }

    private boolean isExcluded(Path dir) {
        Path relativePath = rootPath.relativize(dir);
        return excludePaths.test(relativePath);
    }

    private boolean isHidden(Path path) {
        String dirname = path.getFileName().toString();
        boolean isHiddenDirectory = dirname.startsWith(".");
        return isHiddenDirectory || path.toFile().isHidden();
    }

    private String toProjectPath(Path dir) {
        IncludePath includePath = new IncludePath(rootPath.relativize(dir));
        return includePath.getValue();
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

    private String toIncludeBuildPath(Path includeBuildAbsolutePath) {
        return new IncludeBuildPath(rootPath.relativize(includeBuildAbsolutePath)).getValue();
    }


    public List<String> getIncludeBuildPaths() {
        return includeBuildPaths;
    }

    public List<String> getIncludePaths() {
        return includePaths;
    }
}
