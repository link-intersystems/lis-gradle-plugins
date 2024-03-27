package com.link_intersystems.git.test;

import java.time.ZonedDateTime;
import java.util.function.Consumer;

public interface CommitBuilder {

    void withContent(Consumer<CommitContentBuilder> commitContentBuilder);

    void setAuthor(Person author);

    void setAutorDateTime(ZonedDateTime zonedDateTime);

    void setCommitter(Person committer);

    void setCommitDateTime(ZonedDateTime zonedDateTime);

    void setMessage(String messge);
}
