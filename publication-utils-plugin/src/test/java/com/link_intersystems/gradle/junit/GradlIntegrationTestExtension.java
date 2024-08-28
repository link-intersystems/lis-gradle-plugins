package com.link_intersystems.gradle.junit;

import com.link_intersystems.gradle.project.builder.GradleProjectBuilder;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.util.AnnotationUtils;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;
import java.util.concurrent.Callable;

class GradlIntegrationTestExtension implements ParameterResolver, BeforeAllCallback, AfterAllCallback {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return isGradleRunnerType(parameterContext) || isGradleRunnerCallableType(parameterContext) || isGradleProjectBuilderType(parameterContext);
    }

    private boolean isGradleProjectBuilderType(ParameterContext parameterContext) {
        return GradleProjectBuilder.class.isAssignableFrom(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        try {
            if (isGradleRunnerType(parameterContext)) {
                return getGradleRunner(extensionContext);
            } else if (isGradleRunnerCallableType(parameterContext)) {
                return getGradleRunnerCallable(extensionContext);
            } else if (isGradleProjectBuilderType(parameterContext)) {
                return getGradleProjectBuilder(extensionContext);
            }
        } catch (IOException e) {
            throw new ParameterResolutionException("Unable to resolve parameter " + parameterContext.getParameter(), e);
        }
        return null;
    }

    private GradleProjectBuilder getGradleProjectBuilder(ExtensionContext extensionContext) throws IOException {
        ExtensionStore extensionStore = getExtensionStore(extensionContext);

        GradleProjectBuilder gradleProjectBuilder = extensionStore.getGradleProjectBuilder();
        if (gradleProjectBuilder == null) {
            Path tempDirectory = getTempDirectory(extensionStore);
            gradleProjectBuilder = GradleProjectBuilder.rootProject(tempDirectory);
            extensionStore.setGradleProjectBuilder(gradleProjectBuilder);
        }

        return gradleProjectBuilder;
    }

    private boolean isGradleRunnerCallableType(ParameterContext parameterContext) {
        Parameter parameter = parameterContext.getParameter();
        if (!Callable.class.isAssignableFrom(parameter.getType())) {
            return false;
        }

        Type type = parameterContext.getParameter().getParameterizedType();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length == 1) {
                Type callableResultType = actualTypeArguments[0];
                if (callableResultType instanceof Class<?>) {
                    Class<?> callableResultClass = (Class<?>) callableResultType;
                    return GradleBuildResult.class.isAssignableFrom(callableResultClass);
                }
            }
        }

        return false;
    }

    private GradleRunner getGradleRunner(ExtensionContext extensionContext) throws IOException {
        ExtensionStore extensionStore = getExtensionStore(extensionContext);

        GradleRunner gradleRunner = extensionStore.getGradleRunner();
        if (gradleRunner == null) {
            gradleRunner = createGradleRunner(extensionStore);
        }

        return gradleRunner;
    }

    private GradleRunner createGradleRunner(ExtensionStore extensionStore) throws IOException {
        URI uri = extensionStore.getGradleDistributionUri();
        Path tempDirectory = getTempDirectory(extensionStore);
        GradleRunner gradleRunner = GradleRunner.create()
                .withPluginClasspath()
                .withProjectDir(tempDirectory.toFile())
                .forwardOutput();

        if (uri != null) {
            gradleRunner.withGradleDistribution(uri);
        }

        return gradleRunner;
    }

    private Path getTempDirectory(ExtensionStore extensionStore) throws IOException {
        Path tempDirectory = extensionStore.getTempDirectory();

        if (tempDirectory == null) {
            tempDirectory = Files.createTempDirectory("gradle-integration-test");
            extensionStore.setTempDirectory(tempDirectory);
        }

        return tempDirectory;
    }

    private Callable<GradleBuildResult> getGradleRunnerCallable(ExtensionContext extensionContext) throws IOException {
        ExtensionStore extensionStore = getExtensionStore(extensionContext);

        Callable<GradleBuildResult> gradleRunnerCallable = extensionStore.getGradleRunnerCallable();
        if (gradleRunnerCallable == null) {
            GradleRunner gradleRunner = getGradleRunner(extensionContext);
            gradleRunnerCallable = () -> new TestkitGradleBuildResult(gradleRunner.build());
            extensionStore.setGradleRunnerCallable(gradleRunnerCallable);
        }

        return gradleRunnerCallable;
    }

    private ExtensionStore getExtensionStore(ExtensionContext extensionContext) {
        return ExtensionStore.getInstance(extensionContext);
    }

    private boolean isGradleRunnerType(ParameterContext parameterContext) {
        return GradleRunner.class.isAssignableFrom(parameterContext.getParameter().getType());
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        Class<?> requiredTestClass = extensionContext.getRequiredTestClass();
        Optional<GradleIntegrationTest> gradleIntegrationTestAnnotation = AnnotationUtils.findAnnotation(requiredTestClass, GradleIntegrationTest.class);
        GradleIntegrationTest gradleIntegrationTest = gradleIntegrationTestAnnotation.get();
        String distributionUrl = gradleIntegrationTest.distributionUrl();
        if (!distributionUrl.isEmpty()) {
            URI distributionUri = URI.create(distributionUrl);
            ExtensionStore extensionStore = ExtensionStore.getInstance(extensionContext);
            extensionStore.setGradleDistributionUri(distributionUri);
        }
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        ExtensionStore extensionStore = ExtensionStore.getInstance(extensionContext);
        Path tempDirectory = extensionStore.getTempDirectory();
        if (tempDirectory != null) {
            delete(tempDirectory);
        }
    }

    private void delete(Path directory) throws IOException {
        Files.walkFileTree(directory, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

}
