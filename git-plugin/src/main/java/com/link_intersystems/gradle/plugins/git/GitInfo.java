package com.link_intersystems.gradle.plugins.git;

import java.time.ZonedDateTime;

/**
 * The GitInfo can be used to access the git info of the HEAD pointer.
 * If no branch exists, e.g. an empty repository, <code>null</code> is returned.
 *
 * <pre>
 * plugins {
 *     id("com.link-intersystems.gradle.git") version "0.2.0"
 * }
 *
 * println("""
 * commit ${gitInfo.commitId} (HEAD -&gt; ${gitInfo.branch})
 * Author:     ${gitInfo.authorName} &lt;${gitInfo.authorEmail}&gt;
 * AuthorDate: ${gitInfo.authorDateTime}
 * Commit:     ${gitInfo.committerName} &lt;${gitInfo.committerEmail}&gt;
 * CommitDate: ${gitInfo.commitDateTime}
 *
 * ${gitInfo.commitMessage}
 * """.trimIndent())
 * </pre>
 */
public interface GitInfo {

    /**
     * @return the branch name. In case of a detached HEAD the commit id.
     */
    public String getBranch();

    /**
     * @return the commitId.
     */
    public String getCommitId();

    /**
     * @return the abbreviated commit id (7 characters).
     */
    String getShortCommitId();

    /**
     * @return the commit author name.
     */
    String getAuthorName();

    /**
     * @return the commit author email.
     */
    String getAuthorEmail();

    /**
     * @return the commit author date time.
     */
    ZonedDateTime getAuthorDateTime();

    /**
     * @return the committer name.
     */
    String getCommitterName();

    /**
     * @return the committer email.
     */
    String getCommitterEmail();

    /**
     * @return the commit date time.
     */
    ZonedDateTime getCommitDateTime();

    /**
     * @return the commit message.
     */
    String getCommitMessage();
}
