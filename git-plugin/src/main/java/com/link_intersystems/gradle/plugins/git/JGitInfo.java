package com.link_intersystems.gradle.plugins.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.eclipse.jgit.lib.Constants.HEAD;
import static org.eclipse.jgit.lib.Constants.OBJECT_ID_ABBREV_STRING_LENGTH;

class JGitInfo implements GitInfo {

    private Git git;

    private RevCommit headCommit;

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

    @Override
    public String getCommitId() {
        ObjectId headId = getHeadId();

        if (!ObjectId.zeroId().equals(headId)) {
            return headId.name();
        }

        return null;
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
        ObjectId headId = getHeadId();

        if (!ObjectId.zeroId().equals(headId)) {
            return headId.abbreviate(OBJECT_ID_ABBREV_STRING_LENGTH).name();
        }

        return null;
    }

    private RevCommit getHeadRevCommit() {

        ObjectId currentHead = getHeadId();

        if (ObjectId.zeroId().equals(currentHead)) {
            return null;
        }

        if (headCommit == null || !Objects.equals(headCommit.getId(), currentHead)) {
            headCommit = parseCommit(currentHead);
        }

        return headCommit;
    }

    private RevCommit parseCommit(ObjectId commitId) {
        RevWalk revWalk = getRevWalk();
        try {
            return revWalk.parseCommit(commitId);
        } catch (IOException e) {
            throw new RuntimeException("Unable to get HEAD commit.", e);
        }
    }

    private RevWalk getRevWalk() {
        return new RevWalk(git.getRepository());
    }

    private ObjectId getHeadId() {
        try {
            ObjectId resolvedHead = git.getRepository().resolve(HEAD);
            if (resolvedHead == null) {
                return ObjectId.zeroId();
            }
            return resolvedHead;
        } catch (IOException e) {
            throw new RuntimeException("Unable to resolve HEAD", e);
        }
    }

    @Override
    public List<String> getTags() {
        List<String> tags = new ArrayList<>();
        ObjectId headId = getHeadId();


        List<Ref> tagRefs = getPeeledRefs(Constants.R_TAGS);

        for (Ref tagRef : tagRefs) {
            ObjectId tagCommitId = getCommitId(tagRef);

            if (Objects.equals(tagCommitId, headId)) {
                tags.add(tagRef.getName().substring(Constants.R_TAGS.length()));
            }
        }

        Collections.sort(tags);

        return tags;
    }

    private ObjectId getCommitId(Ref tagRef) {
        ObjectId commitId = tagRef.getPeeledObjectId();

        if (commitId == null) {
            commitId = tagRef.getObjectId();
        }

        return commitId;
    }

    private List<Ref> getPeeledRefs(String prefix) {
        RefDatabase refDatabase = git.getRepository().getRefDatabase();
        try {
            List<Ref> peeledRefs = new ArrayList<>();

            for (Ref byPrefix : refDatabase.getRefsByPrefix(prefix)) {
                Ref peeledRef = refDatabase.peel(byPrefix);
                peeledRefs.add(peeledRef);
            }

            return peeledRefs;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
