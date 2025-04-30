package com.link_intersystems.gradle.project.plugin;

import com.link_intersystems.gradle.project.GradleBuildFilePredicate;
import com.link_intersystems.gradle.project.GradleSettingsPredicate;
import com.link_intersystems.gradle.project.composite.IncludeBuildPath;
import com.link_intersystems.gradle.project.sub.IncludePath;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Objects.*;

class IncludesCollector {

    private List<IncludeBuildPath> includeBuildPaths = new ArrayList<>();
    private List<IncludePath> includePaths = new ArrayList<>();

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

    private IncludePath toProjectPath(Path dir) {
        return new IncludePath(rootPath.relativize(dir));
    }


    private IncludeBuildPath toIncludeBuildPath(Path includeBuildAbsolutePath) {
        return new IncludeBuildPath(rootPath.relativize(includeBuildAbsolutePath));
    }


    public List<IncludeBuildPath> getIncludeBuildPaths() {
        return includeBuildPaths;
    }

    public List<IncludePath> getIncludePaths() {
        return includePaths;
    }

    public void collect() throws IOException {
        List<Path> dirs = new ArrayList<>();
        Files.walkFileTree(rootPath, new FileVisitor<>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                if (dir.equals(rootPath)) {
                    return FileVisitResult.CONTINUE;
                }

                if (skipDir(dir)) {
                    return FileVisitResult.SKIP_SUBTREE;
                }

                if (isCompositeBuildDir(dir)) {
                    dirs.add(dir);
                    return FileVisitResult.SKIP_SUBTREE;
                }

                if (isSubmoduleDir(dir)) {
                    dirs.add(dir);
                    return FileVisitResult.CONTINUE;
                }

                return FileVisitResult.CONTINUE;
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
        });

        Comparator<? super Path> pathOrder = (p1, p2) -> {
            if (Files.isDirectory(p1)) {
                if (Files.isDirectory(p2)) {
                    return p1.compareTo(p2);
                }
                return 1;
            }
            if (Files.isDirectory(p2)) {
                return -1;
            }
            return p1.compareTo(p2);
        };

        try (Stream<Path> stream = dirs.stream().sorted(pathOrder)) {
            stream.forEach(path -> {
                if (isCompositeBuildDir(path)) {
                    includeBuildPaths.add(toIncludeBuildPath(path));
                } else if (isSubmoduleDir(path)) {
                    includePaths.add(toProjectPath(path));
                }
            });
        }
    }
}
