package com.link_intersystems.gradle.plugins.publication.verify;

import com.link_intersystems.gradle.plugins.publication.PublicationFilter;

public class VerifyPublicationExtension {

    private VerifyMode verifyMode;

    private PublicationFilter publicationFilter;

    public VerifyMode getMode() {
        return verifyMode;
    }

    /**
     * Sets the strategy for handling the result of a check in the context of artifact publication.
     * This method allows you to specify how the result of a {@link VerifyPublicationTask} should be handled.
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
     * The default is {@link VerifyModes#NONE_EXISTS}
     *
     * @see VerifyPublicationExtension
     * @see VerifyMode
     */
    public void setMode(VerifyMode verifyMode) {
        this.verifyMode = verifyMode;
    }

    public void setPublicationFilter(PublicationFilter publicationFilter) {
        this.publicationFilter = publicationFilter;
    }

    public PublicationFilter getPublicationFilter() {
        return publicationFilter;
    }
}

