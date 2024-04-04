package com.link_intersystems.gradle.plugins.multimodule;

/**
 * A {@link java.util.function.Predicate}&lt;{@link java.nio.file.Path}&gt; that evaluates to true, if the path
 * is a directory and contains a settings gradle file. Supported file extensions depend on the scripting languages
 * that gradle supports. Usually groovy or gradle
 * <ul>
 *     <li>settings.gradle</li>
 *     <li>settings.gradle.kts</li>
 * </ul>
 */
class GradleSettingsPredicate extends AbstractGradleFilePredicate {

    public GradleSettingsPredicate() {
        super("settings");
    }

}
