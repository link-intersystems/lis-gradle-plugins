package com.link_intersystems.gradle.project;

import org.gradle.internal.scripts.DefaultScriptFileResolver;
import org.gradle.internal.scripts.ScriptFileResolver;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public abstract class AbstractGradleFilePredicate implements Predicate<Path> {

    private final ScriptFileResolver scriptFileResolver = new DefaultScriptFileResolver();

    private String fileBasename;

    public AbstractGradleFilePredicate(String fileBasename) {
        this.fileBasename = requireNonNull(fileBasename);
    }

    @Override
    public boolean test(Path path) {
        File buildScriptFile = scriptFileResolver.resolveScriptFile(path.toFile(), fileBasename);
        return buildScriptFile != null;
    }
}
