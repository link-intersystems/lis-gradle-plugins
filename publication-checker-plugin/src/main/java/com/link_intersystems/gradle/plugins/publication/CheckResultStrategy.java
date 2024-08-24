package com.link_intersystems.gradle.plugins.publication;

import java.util.List;

public interface CheckResultStrategy {
    void handle(PublicationCheckResult publicationCheckResult);
}
