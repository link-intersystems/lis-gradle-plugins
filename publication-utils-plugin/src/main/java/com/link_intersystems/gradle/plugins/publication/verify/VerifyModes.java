package com.link_intersystems.gradle.plugins.publication.verify;

import com.link_intersystems.gradle.plugins.publication.VerifyPublicationArtifactResult;
import com.link_intersystems.gradle.plugins.publication.VerifyPublicationResult;
import org.gradle.api.GradleException;
import org.gradle.api.logging.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public enum VerifyModes implements VerifyMode {

    ALL_EXIST() {
        @Override
        public void handle(VerifyPublicationResult verifyPublicationResult) {
            List<VerifyPublicationArtifactResult> verifyPublicationArtifactResults = verifyPublicationResult.getArtifactCheckResults();
            Boolean noOneIsMissing = verifyPublicationArtifactResults.stream().map(VerifyPublicationArtifactResult::isSuccess).reduce((b1, b2) -> b1 && b2).orElse(false);

            if (!noOneIsMissing) {
                StringBuilder sb = new StringBuilder();
                sb.append("Expected all artifacts exist in ");
                sb.append(verifyPublicationResult.getArtifactRepositoryDesc());
                sb.append(" but:\n");
                for (VerifyPublicationArtifactResult verifyPublicationArtifactResult : verifyPublicationArtifactResults) {
                    sb.append(verifyPublicationArtifactResult.getArtifact());
                    if (verifyPublicationArtifactResult.isSuccess()) {
                        sb.append(" EXIST");
                    } else {
                        sb.append(" MISSING");
                    }
                    sb.append(System.lineSeparator());
                }

                throw new GradleException(sb.toString());
            }
        }
    },
    NONE_EXISTS() {
        @Override
        public void handle(VerifyPublicationResult verifyPublicationResult) {
            List<VerifyPublicationArtifactResult> verifyPublicationArtifactResults = verifyPublicationResult.getArtifactCheckResults();
            Boolean atLeatOneExists = verifyPublicationArtifactResults.stream().map(VerifyPublicationArtifactResult::isSuccess).reduce((b1, b2) -> b1 || b2).orElse(false);

            if (atLeatOneExists) {
                StringBuilder sb = new StringBuilder();
                sb.append("Expected no artifacts exist in ");
                sb.append(verifyPublicationResult.getArtifactRepositoryDesc());
                sb.append(" but:\n");
                for (VerifyPublicationArtifactResult verifyPublicationArtifactResult : verifyPublicationArtifactResults) {
                    sb.append("\t- ");
                    sb.append(verifyPublicationArtifactResult.getArtifact());
                    if (verifyPublicationArtifactResult.isSuccess()) {
                        sb.append(" EXIST");
                    } else {
                        sb.append(" MISSING");
                    }
                    sb.append(System.lineSeparator());
                }

                throw new GradleException(sb.toString());
            }
        }
    },
    REPORT_ONLY() {

        private Logger logger = (Logger) LoggerFactory.getLogger(VerifyModes.class);

        @Override
        public void handle(VerifyPublicationResult verifyPublicationResult) {
            StringBuilder sb = new StringBuilder();
            sb.append("Artifacts in ");
            sb.append(verifyPublicationResult.getArtifactRepositoryDesc());
            sb.append(":\n");
            for (VerifyPublicationArtifactResult verifyPublicationArtifactResult : verifyPublicationResult.getArtifactCheckResults()) {
                sb.append("\t- ");
                sb.append(verifyPublicationArtifactResult.getArtifact());
                if (verifyPublicationArtifactResult.isSuccess()) {
                    sb.append(" EXIST");
                } else {
                    sb.append(" MISSING");
                }
                sb.append(System.lineSeparator());
            }

            logger.lifecycle(sb.toString());
        }
    }
}
