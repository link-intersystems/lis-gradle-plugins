package com.link_intersystems.gradle.plugins.multimodule;

/**
 * A {@link java.util.function.Predicate}&lt;{@link java.nio.file.Path}&gt; that evaluates to true, if the path
 * is a directory and contains a build gradle file. Supported file extensions depend on the scripting languages
 * that gradle supports. Usually groovy or gradle
 * <ul>
 *     <li>build.gradle</li>
 *     <li>build.gradle.kts</li>
 * </ul>
 */
class GradleBuildFilePredicate extends AbstractGradleFilePredicate {

    public GradleBuildFilePredicate() {
        super("build");
    }
}
