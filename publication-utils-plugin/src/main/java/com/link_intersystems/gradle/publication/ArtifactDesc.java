package com.link_intersystems.gradle.publication;

public class ArtifactDesc {
    private final String group;
    private final String name;
    private String version;

    public ArtifactDesc(String group, String name, String version) {
        this.group = group;
        this.name = name;
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
