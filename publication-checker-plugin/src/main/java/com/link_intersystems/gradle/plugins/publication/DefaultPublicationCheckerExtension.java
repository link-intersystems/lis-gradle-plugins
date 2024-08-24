package com.link_intersystems.gradle.plugins.publication;

public class DefaultPublicationCheckerExtension implements PublicationCheckerExtension {

    private CheckResultStrategy checkResultStrategy;

    private PublicationCheckFilter publicationFilter;

    @Override
    public CheckResultStrategy getCheckResultStrategy() {
        return checkResultStrategy;
    }

    /**
     * Sets the strategy for handling the result of a check in the context of artifact publication.
     * This method allows you to specify how the result of a {@link PublicationCheckerTask} should be handled.
     * <p>
     * You can use the values defined as an enum called {@code CheckResultStrategies}:
     * - {@code FAIL_IF_MISSING}: This strategy logs a message and throws a {@link org.gradle.api.GradleException} if the artifact is missing.
     * - {@code FAIL_IF_EXISTENT}: This strategy logs a message and throws a {@link org.gradle.api.GradleException} if the artifact already exists.
     * - {@code LOG_ONLY}: This strategy logs a message without throwing any exception.
     * <p>
     * or use your own implementation, e.g. in your <code>build.gradle.kts</code>
     * <pre>
     * publicationChecker {
     *     checkResultStrategy = CheckResultStrategy { publication, exists -&gt;
     *         println("--- " + publication.artifact)
     *     }
     * }
     * </pre>
     * <p>
     * The default is {@link CheckResultStrategies#FAIL_IF_EXISTENT}
     *
     * @see DefaultPublicationCheckerExtension
     * @see CheckResultStrategy
     */
    @Override
    public void setCheckResultStrategy(CheckResultStrategy checkResultStrategy) {
        this.checkResultStrategy = checkResultStrategy;
    }

    @Override
    public void setPublicationFilter(PublicationCheckFilter publicationFilter) {
        this.publicationFilter = publicationFilter;
    }

    @Override
    public PublicationCheckFilter getPublicationFilter() {
        return publicationFilter;
    }
}

