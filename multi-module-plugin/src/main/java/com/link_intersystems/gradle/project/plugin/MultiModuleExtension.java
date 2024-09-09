package com.link_intersystems.gradle.project.plugin;

import java.util.List;

public class MultiModuleExtension implements ConfigValues {

    private List<String> excludedPaths;
    private Boolean omitDefaultExcludes;
    private Boolean dryRun;

    @Override
    public List<String> getExcludedPaths() {
        return excludedPaths;
    }


    /**
     * Exclude project paths that should not be included.
     * <p>
     * Let's assume you have a project structure like this.
     * <pre>
     * my-app/
     * +- modules/
     * |  +- moduleA/
     * |  |  +- build.gradle.kts
     * |  |- moduleB/
     * |     +- build.gradle.kts
     * +- otherModules/
     * |  +- moduleA/
     * |  |  +- build.gradle.kts
     * +- build.gradle.kts
     * +- gradle.properties
     * +- settings.gradle.kts
     * </pre>
     * <p>
     * Examples
     * <pre>
     * // settings.gradle.kts
     * import com.link_intersystems.gradle.project.plugin.MultiModuleConfig
     *
     * // Exclude only modules/moduleA
     * configure&lt;MultiModuleConfig&gt; {
     *     excludedPaths = listOf("modules/moduleA")
     * }
     *
     * // Exclude all modules ending with A (modules/moduleA, otherModules/moduleA)
     * configure&lt;MultiModuleConfig&gt; {
     *     excludedPaths = listOf("**&#47;*A")
     * }
     *
     * // Exclude modules/moduleA and modules/moduleB
     * configure&lt;MultiModuleConfig&gt; {
     *     excludedPaths = listOf("regex:modules/module?")
     * }
     * </pre>
     * <p>
     * You can apply any value a {@link java.nio.file.FileSystem#getPathMatcher(String)} accepts. See  for details.
     * If you do not prefix an exclude path with <code>glob:</code> or <code>regex:</code>, the plugin assumes that the path is a glob pattern.
     *
     * @param excludedPaths the glob or regexp pattern to exclude a module path. See {@link java.nio.file.FileSystem#getPathMatcher(String)} for
     *                      details.
     */
    public void setExcludedPaths(List<String> excludedPaths) {
        this.excludedPaths = excludedPaths;
    }

    @Override
    public Boolean getOmitDefaultExcludes() {
        return omitDefaultExcludes;
    }

    @Override
    public Boolean getDryRun() {
        return dryRun;
    }

    public void setDryRun(Boolean dryRun) {
        this.dryRun = dryRun;
    }

    /**
     * The default excludes are locations that are usually used to implement convention plugins, such as <code>buildSrc</code>
     * and any <code>includeBuild</code> of a <code>pluginManagement</code>. These locations are excluded per default.
     * However, you can turn off the default excludes by setting this property to <code>false</code>.
     *
     * @param omitDefaultExcludes false to turn off default excludes. True is the default.
     */
    public void setOmitDefaultExcludes(boolean omitDefaultExcludes) {
        this.omitDefaultExcludes = omitDefaultExcludes;
    }
}
