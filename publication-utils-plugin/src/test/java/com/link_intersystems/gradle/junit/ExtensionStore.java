package com.link_intersystems.gradle.junit;

import com.link_intersystems.gradle.project.builder.GradleProjectBuilder;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.net.URI;
import java.nio.file.Path;
import java.util.concurrent.Callable;

class ExtensionStore {

    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ExtensionStore.class);

    public static ExtensionStore getInstance(ExtensionContext context) {
        return new ExtensionStore(context.getStore(NAMESPACE));
    }

    private ExtensionContext.Store store;

    public ExtensionStore(ExtensionContext.Store store) {
        this.store = store;
    }

    public void setGradleRunner(GradleRunner gradleRunner) {
        store.put("GRADLE_RUNNER", gradleRunner);
    }

    public GradleRunner getGradleRunner() {
        return store.get("GRADLE_RUNNER", GradleRunner.class);
    }

    public URI getGradleDistributionUri() {
        return store.get("GRADLE_DIST_URI", URI.class);
    }

    public void setGradleDistributionUri(URI customGradleDistributionUri) {
        store.put("GRADLE_DIST_URI", customGradleDistributionUri);
    }

    @SuppressWarnings("unchecked")
    public Callable<GradleBuildResult> getGradleRunnerCallable() {
        return store.get("GRADLE_RUNNER_CALLABLE", Callable.class);
    }

    public void setGradleRunnerCallable(Callable<GradleBuildResult> gradleRunnerCallable) {
        store.put("GRADLE_RUNNER_CALLABLE", gradleRunnerCallable);
    }

    public GradleProjectBuilder getGradleProjectBuilder() {
        return store.get("GRADLE_PROJECT_BUILDER", GradleProjectBuilder.class);
    }

    public void setGradleProjectBuilder(GradleProjectBuilder gradleProjectBuilder) {
        store.put("GRADLE_PROJECT_BUILDER", gradleProjectBuilder);
    }

    public Path getTempDirectory() {
        return store.get("TEMP_DIR", Path.class);
    }

    public void setTempDirectory(Path tempDirectory) {
        store.put("TEMP_DIR", tempDirectory);
    }

}
