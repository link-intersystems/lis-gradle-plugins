package com.link_intersystems.gradle.plugins.multimodule;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class IncludesCollector implements FileVisitor<Path> {

    private List<String> includeBuildPaths = new ArrayList<>();
    private List<String> includePaths = new ArrayList<>();

    private GradleRootProjectPredicate rootProjectPredicate = new GradleRootProjectPredicate();
    private GradleProjectPredicate gradleProjectPredicate = new GradleProjectPredicate();

    private Path rootPath;
    private final List<String> filterPaths;

    public IncludesCollector(Path rootPath, List<String> filterPaths) {
        this.rootPath = rootPath;
        this.filterPaths = filterPaths;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if (dir.equals(rootPath)) {
            return FileVisitResult.CONTINUE;
        }

        if (attrs.isDirectory()) {
            String dirname = dir.getFileName().toString();
            boolean isHiddenDirectory = dirname.startsWith(".");
            if (isHiddenDirectory || dir.toFile().isHidden()) {
                return FileVisitResult.SKIP_SUBTREE;
            }

            Path relativePath = rootPath.relativize(dir);

            if (filterPaths.contains(relativePath.toString())) {
                return FileVisitResult.SKIP_SUBTREE;
            }
        }


        if (rootProjectPredicate.test(dir)) {
            includeBuildPaths.add(toIncludeBuildPath(dir));
            return FileVisitResult.SKIP_SUBTREE;
        }

        if (gradleProjectPredicate.test(dir)) {
            includePaths.add(toProjectPath(dir));
            return FileVisitResult.SKIP_SUBTREE;
        }

        return FileVisitResult.CONTINUE;
    }

    private String toProjectPath(Path dir) {
        String includeBuildPath = toIncludeBuildPath(dir);
        return ":" + includeBuildPath.replace('/', ':');
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    private String toIncludeBuildPath(Path includeBuildAbsolutePath) {
        return rootPath.relativize(includeBuildAbsolutePath).toString().replace('\\', '/');
    }


    public List<String> getIncludeBuildPaths() {
        return includeBuildPaths;
    }

    public List<String> getIncludePaths() {
        return includePaths;
    }
}
