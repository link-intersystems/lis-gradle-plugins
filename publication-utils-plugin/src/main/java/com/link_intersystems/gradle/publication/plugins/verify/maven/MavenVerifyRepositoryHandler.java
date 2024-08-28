package com.link_intersystems.gradle.publication.plugins.verify.maven;

import com.link_intersystems.gradle.publication.plugins.verify.VerifyRepositoryHandler;
import groovy.lang.Closure;
import org.gradle.api.Action;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;

public interface MavenVerifyRepositoryHandler extends VerifyRepositoryHandler {
    MavenArtifactRepository mavenCentral();

    MavenArtifactRepository mavenCentral(Action<? super MavenArtifactRepository> action);

    MavenArtifactRepository mavenLocal();

    MavenArtifactRepository mavenLocal(Action<? super MavenArtifactRepository> action);

    MavenArtifactRepository maven(Closure closure);

    MavenArtifactRepository maven(Action<? super MavenArtifactRepository> action);
}
