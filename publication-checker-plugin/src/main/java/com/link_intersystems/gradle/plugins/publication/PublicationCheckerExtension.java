package com.link_intersystems.gradle.plugins.publication;

public interface PublicationCheckerExtension {
    String NAME = "publicationChecker";

    public void setCheckResultStrategy(CheckResultStrategy checkResultHandling);

    public CheckResultStrategy getCheckResultStrategy();

    PublicationCheckFilter getPublicationFilter();

    void setPublicationFilter(PublicationCheckFilter publicationCheckFilter);
}
