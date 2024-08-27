package com.link_intersystems.gradle.plugins.publication.verify;

import com.link_intersystems.gradle.plugins.publication.VerifyPublicationArtifactResult;
import com.link_intersystems.gradle.plugins.publication.VerifyPublicationResult;
import org.gradle.api.GradleException;
import org.gradle.api.logging.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.List;
import java.util.ListIterator;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class VerifyPublicationResultHandlers implements VerifyPublicationResultHandler {

    public static final VerifyPublicationResultHandler ALL_EXIST = new VerifyPublicationResultHandler() {
        @Override
        public void handle(VerifyPublicationResult verifyPublicationResult) {
            List<VerifyPublicationArtifactResult> verifyPublicationArtifactResults = verifyPublicationResult.getArtifactResults();
            Boolean noOneIsMissing = verifyPublicationArtifactResults.stream().map(VerifyPublicationArtifactResult::isSuccess).reduce((b1, b2) -> b1 && b2).orElse(false);

            if (!noOneIsMissing) {
                CharSequence sb = message(verifyPublicationResult, "Expected all artifacts exist in {0} but:\n");
                throw new GradleException(sb.toString());
            }
        }
    };

    public static final VerifyPublicationResultHandler NONE_EXISTS = new VerifyPublicationResultHandler() {
        @Override
        public void handle(VerifyPublicationResult verifyPublicationResult) {
            List<VerifyPublicationArtifactResult> verifyPublicationArtifactResults = verifyPublicationResult.getArtifactResults();
            Boolean atLeatOneExists = verifyPublicationArtifactResults.stream().map(VerifyPublicationArtifactResult::isSuccess).reduce((b1, b2) -> b1 || b2).orElse(false);

            if (atLeatOneExists) {
                CharSequence message = message(verifyPublicationResult, "Expected no artifacts exist in {0} but:\n");
                throw new GradleException(message.toString());
            }
        }
    };

    public static final VerifyPublicationResultHandler REPORT_ONLY = new VerifyPublicationResultHandler() {

        private Logger logger = (Logger) LoggerFactory.getLogger(VerifyPublicationResultHandlers.class);

        @Override
        public void handle(VerifyPublicationResult verifyPublicationResult) {
            CharSequence message = message(verifyPublicationResult, "Artifacts in {0}:\n");
            logger.lifecycle(message.toString());
        }
    };

    private static CharSequence message(VerifyPublicationResult verifyPublicationResult, String messageFormat) {
        StringBuilder sb = new StringBuilder();
        sb.append(MessageFormat.format(messageFormat, verifyPublicationResult.getArtifactRepositoryDesc()));
        sb.append(VerifyPublicationResultHandlers.listResults(verifyPublicationResult.getArtifactResults()));
        return sb;
    }

    private static CharSequence listResults(List<VerifyPublicationArtifactResult> verifyPublicationArtifactResults) {
        return listResults(verifyPublicationArtifactResults, Object::toString, System.lineSeparator(), (r, index) -> "\t ");
    }

    private static CharSequence listResults(List<VerifyPublicationArtifactResult> verifyPublicationArtifactResults, Function<VerifyPublicationArtifactResult, String> verifyPublicationArtifactResultRenderer, String separator, BiFunction<VerifyPublicationArtifactResult, Integer, String> prefixFunction) {
        StringBuilder sb = new StringBuilder();
        ListIterator<VerifyPublicationArtifactResult> iterator = verifyPublicationArtifactResults.listIterator();
        while (iterator.hasNext()) {
            VerifyPublicationArtifactResult next = iterator.next();
            String prefix = prefixFunction.apply(next, iterator.previousIndex());
            sb.append(prefix);
            sb.append(verifyPublicationArtifactResultRenderer.apply(next));
            if (iterator.hasNext()) {
                sb.append(separator);
            }
        }
        return sb;
    }
}
