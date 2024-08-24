package com.link_intersystems.gradle.plugins.publication;

import org.gradle.api.GradleException;
import org.gradle.api.logging.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public enum CheckResultStrategies implements CheckResultStrategy {

    FAIL_IF_MISSING() {
        @Override
        public void handle(PublicationCheckResult publicationCheckResult) {
            List<ArtifactCheckResult> artifactCheckResults = publicationCheckResult.getArtifactCheckResults();
            Boolean noOneIsMissing = artifactCheckResults.stream().map(ArtifactCheckResult::isExistent).reduce((b1, b2) -> b1 && b2).orElse(false);

            if (!noOneIsMissing) {
                StringBuilder sb = new StringBuilder();
                sb.append("Expected all artifacts exist in ");
                sb.append(publicationCheckResult.getArtifactRepositoryDesc());
                sb.append(" but:\n");
                for (ArtifactCheckResult artifactCheckResult : artifactCheckResults) {
                    sb.append(artifactCheckResult.getArtifact());
                    if (artifactCheckResult.isExistent()) {
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
    FAIL_IF_EXISTENT() {
        @Override
        public void handle(PublicationCheckResult publicationCheckResult) {
            List<ArtifactCheckResult> artifactCheckResults = publicationCheckResult.getArtifactCheckResults();
            Boolean atLeatOneExists = artifactCheckResults.stream().map(ArtifactCheckResult::isExistent).reduce((b1, b2) -> b1 || b2).orElse(false);

            if (atLeatOneExists) {
                StringBuilder sb = new StringBuilder();
                sb.append("Expected no artifacts exist in ");
                sb.append(publicationCheckResult.getArtifactRepositoryDesc());
                sb.append(" but:\n");
                for (ArtifactCheckResult artifactCheckResult : artifactCheckResults) {
                    sb.append("\t- ");
                    sb.append(artifactCheckResult.getArtifact());
                    if (artifactCheckResult.isExistent()) {
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
    LOG_ONLY() {

        private Logger logger = (Logger) LoggerFactory.getLogger(CheckResultStrategies.class);

        @Override
        public void handle(PublicationCheckResult publicationCheckResult) {
            StringBuilder sb = new StringBuilder();
            sb.append("Artifacts in ");
            sb.append(publicationCheckResult.getArtifactRepositoryDesc());
            sb.append(":\n");
            for (ArtifactCheckResult artifactCheckResult : publicationCheckResult.getArtifactCheckResults()) {
                sb.append("\t- ");
                sb.append(artifactCheckResult.getArtifact());
                if (artifactCheckResult.isExistent()) {
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
