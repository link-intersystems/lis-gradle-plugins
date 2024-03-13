package com.link_intersystems.gradle.plugins.multimodule;

import org.gradle.internal.scripts.DefaultScriptFileResolver;
import org.gradle.internal.scripts.ScriptFileResolver;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Predicate;

public class GradleProjectPredicate implements Predicate<Path> {

    private final ScriptFileResolver scriptFileResolver = new DefaultScriptFileResolver();

    @Override
    public boolean test(Path path) {
        File buildScriptFile = scriptFileResolver.resolveScriptFile(path.toFile(), "build");
        return buildScriptFile != null;
    }


}
