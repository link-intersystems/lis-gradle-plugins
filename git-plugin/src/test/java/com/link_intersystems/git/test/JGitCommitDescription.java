package com.link_intersystems.git.test;


import org.eclipse.jgit.lib.PersonIdent;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.function.Consumer;

public class JGitCommitDescription implements CommitBuilder {

    private final Person author;
    private Person authorOverride;
    private final Person committer;
    private final CommitContentBuilder commitContentBuilder;
    private Person committerOverride;
    private ZonedDateTime authorDateTime;
    private ZonedDateTime committerDateTime;
    private String message;

    public JGitCommitDescription(Person author, Person committer, CommitContentBuilder commitContentBuilder) {

        this.author = author;
        this.committer = committer;
        this.commitContentBuilder = commitContentBuilder;
    }

    @Override
    public void withContent(Consumer<CommitContentBuilder> commitContentBuilderConsumer) {
        commitContentBuilderConsumer.accept(commitContentBuilder);
    }

    @Override
    public void setAuthor(Person author) {
        this.authorOverride = author;
    }


    @Override
    public void setAutorDateTime(ZonedDateTime authorDateTime) {
        this.authorDateTime = authorDateTime;
    }

    @Override
    public void setCommitter(Person committer) {
        this.committerOverride = committer;
    }

    @Override
    public void setCommitDateTime(ZonedDateTime committerDateTime) {
        this.committerDateTime = committerDateTime;
    }

    public PersonIdent getAuthorIdent() {
        String name = author.getName();
        if (authorOverride != null && authorOverride.getName() != null) {
            name = authorOverride.getName();
        }

        String email = author.getEmail();
        if (authorOverride != null && authorOverride.getEmail() != null) {
            email = authorOverride.getEmail();
        }
        if (authorDateTime != null) {
            Instant authorInstant = authorDateTime.toInstant();
            return new PersonIdent(name, email, authorInstant, authorDateTime.getZone());
        } else {
            return new PersonIdent(name, email);
        }
    }

    public PersonIdent getCommitterIdent() {
        String name = committer.getName();
        if (committerOverride != null && committerOverride.getName() != null) {
            name = committerOverride.getName();
        }

        String email = committer.getEmail();
        if (committerOverride != null && committerOverride.getEmail() != null) {
            email = committerOverride.getEmail();
        }

        if (committerDateTime != null) {
            Instant authorInstant = committerDateTime.toInstant();
            return new PersonIdent(name, email, authorInstant, committerDateTime.getZone());
        } else {
            return new PersonIdent(name, email);
        }
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
