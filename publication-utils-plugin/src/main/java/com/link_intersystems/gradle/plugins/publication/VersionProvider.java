package com.link_intersystems.gradle.plugins.publication;

public interface VersionProvider {

    String getVersion(String group, String name, String version);
}
