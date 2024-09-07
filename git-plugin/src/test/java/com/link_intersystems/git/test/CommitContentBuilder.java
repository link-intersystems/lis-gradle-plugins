package com.link_intersystems.git.test;

public interface CommitContentBuilder {

    void addFile(String path, String content);
    void addAllFiles();
    void addFile(String path);
}
