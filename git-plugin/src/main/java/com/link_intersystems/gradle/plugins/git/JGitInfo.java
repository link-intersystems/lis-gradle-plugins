package com.link_intersystems.gradle.plugins.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;

import static org.eclipse.jgit.lib.Constants.HEAD;
import static org.eclipse.jgit.lib.Constants.OBJECT_ID_ABBREV_STRING_LENGTH;

public class JGitInfo implements GitInfo {

    private Git git;

    private ObjectId latestObjectId;
    private RevCommit latestRevCommit;

    public JGitInfo(Git git) {
        this.git = git;
    }

    @Override
    public String getBranch() {
        try {
            return git.getRepository().getBranch();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private RevCommit getHeadRevCommit() {

        try {
            ObjectId actualHeadId = git.getRepository().resolve(HEAD);
            if (!Objects.equals(latestObjectId, actualHeadId)) {
                latestRevCommit = git.log().setMaxCount(1).add(actualHeadId).call().iterator().next();
                this.latestObjectId = actualHeadId;
            }
        } catch (IOException | GitAPIException e) {
            throw new RuntimeException(e);
        }

        return latestRevCommit;
    }

    @Override
    public String getCommitId() {
        try {
            ObjectId headId = git.getRepository().resolve(HEAD);
            if (headId != null) {
                return headId.name();
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ZonedDateTime getCommitDateTime() {
        RevCommit headCommit = getHeadRevCommit();
        if (headCommit == null) {
            return null;
        }
        PersonIdent committerIdent = headCommit.getCommitterIdent();
        return ZonedDateTime.ofInstant(committerIdent.getWhenAsInstant(), committerIdent.getZoneId());
    }

    @Override
    public String getCommitterName() {
        RevCommit headCommit = getHeadRevCommit();
        if (headCommit == null) {
            return null;
        }
        PersonIdent committerIdent = headCommit.getCommitterIdent();
        return committerIdent.getName();
    }

    @Override
    public String getCommitterEmail() {
        RevCommit headCommit = getHeadRevCommit();
        if (headCommit == null) {
            return null;
        }
        PersonIdent committerIdent = headCommit.getCommitterIdent();
        return committerIdent.getEmailAddress();
    }

    @Override
    public String getAuthorName() {
        RevCommit headCommit = getHeadRevCommit();
        if (headCommit == null) {
            return null;
        }
        PersonIdent authorIdent = headCommit.getAuthorIdent();
        return authorIdent.getName();
    }

    @Override
    public String getAuthorEmail() {
        RevCommit headCommit = getHeadRevCommit();
        if (headCommit == null) {
            return null;
        }
        PersonIdent authorIdent = headCommit.getAuthorIdent();
        return authorIdent.getEmailAddress();
    }

    @Override
    public String getCommitMessage() {
        RevCommit headCommit = getHeadRevCommit();
        if (headCommit == null) {
            return null;
        }
        return headCommit.getFullMessage();
    }

    @Override
    public ZonedDateTime getAuthorDateTime() {
        RevCommit headCommit = getHeadRevCommit();
        if (headCommit == null) {
            return null;
        }
        PersonIdent authorIdent = headCommit.getAuthorIdent();
        return ZonedDateTime.ofInstant(authorIdent.getWhenAsInstant(), authorIdent.getZoneId());
    }

    @Override
    public String getShortCommitId() {
        try {
            ObjectId headId = git.getRepository().resolve(HEAD);
            if (headId != null) {
                return headId.abbreviate(OBJECT_ID_ABBREV_STRING_LENGTH).name();
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
