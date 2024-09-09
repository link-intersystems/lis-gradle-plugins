package com.link_intersystems.gradle.git.jgit;

import com.link_intersystems.git.test.Person;
import com.link_intersystems.git.test.TestRepository;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.time.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class JGitInfoTest {

    private TestRepository testRepository;
    private JGitInfo jGitInfo;

    @BeforeEach
    void setUp(@TempDir File tempDir) throws Exception {
        testRepository = new TestRepository(tempDir);
        jGitInfo = new JGitInfo(testRepository.getGit());
    }

    @Test
    void emptyRepo() {
        assertEquals("main", jGitInfo.getBranch());

        assertNull(jGitInfo.getCommitId());
        assertNull(jGitInfo.getShortCommitId());

        assertNull(jGitInfo.getAuthorEmail());
        assertNull(jGitInfo.getAuthorName());
        assertNull(jGitInfo.getAuthorDateTime());

        assertNull(jGitInfo.getCommitterName());
        assertNull(jGitInfo.getCommitterEmail());
        assertNull(jGitInfo.getCommitDateTime());

        assertNull(jGitInfo.getCommitMessage());
    }

    @Test
    void oneCommit() throws Exception {
        LocalDate authorDate = LocalDate.of(2020, 2, 4);
        LocalTime authorTime = LocalTime.of(15, 32, 12);
        ZoneId authorZoneId = ZoneId.ofOffset("GMT", ZoneOffset.ofHours(1));
        ZonedDateTime authorDateTime = ZonedDateTime.of(authorDate, authorTime, authorZoneId);

        LocalDate committerDate = LocalDate.of(2020, 2, 5);
        LocalTime committerTime = LocalTime.of(12, 12, 5);
        ZoneId committerZoneId = ZoneId.ofOffset("GMT", ZoneOffset.ofHours(3));
        ZonedDateTime committerDateTime = ZonedDateTime.of(committerDate, committerTime, committerZoneId);

        RevCommit revCommit = testRepository.createCommit(commitBuilder -> {
            commitBuilder.setMessage("commit message");
            commitBuilder.setAutorDateTime(authorDateTime);
            commitBuilder.setCommitter(new Person("Jane Doe", "jane.doe@acme.com"));
            commitBuilder.setCommitDateTime(committerDateTime);
        });

        assertEquals("main", jGitInfo.getBranch());

        assertEquals(revCommit.getId().name(), jGitInfo.getCommitId());
        assertEquals(revCommit.getId().abbreviate(7).name(), jGitInfo.getShortCommitId());

        assertEquals("john.doe@acme.com", jGitInfo.getAuthorEmail());
        assertEquals("John Doe", jGitInfo.getAuthorName());
        assertEquals(authorDateTime, jGitInfo.getAuthorDateTime());

        assertEquals("jane.doe@acme.com", jGitInfo.getCommitterEmail());
        assertEquals("Jane Doe", jGitInfo.getCommitterName());
        assertEquals(committerDateTime, jGitInfo.getCommitDateTime());

        assertEquals("commit message", jGitInfo.getCommitMessage());
    }

    @Test
    void twoCommits() throws Exception {
        oneCommit();
        LocalDate authorDate = LocalDate.of(2021, 2, 4);
        LocalTime authorTime = LocalTime.of(12, 3, 8);
        ZoneId authorZoneId = ZoneId.ofOffset("GMT", ZoneOffset.ofHours(1));
        ZonedDateTime authorDateTime = ZonedDateTime.of(authorDate, authorTime, authorZoneId);

        LocalDate committerDate = LocalDate.of(2021, 2, 6);
        LocalTime committerTime = LocalTime.of(17, 47, 5);
        ZoneId committerZoneId = ZoneId.ofOffset("GMT", ZoneOffset.ofHours(2));
        ZonedDateTime committerDateTime = ZonedDateTime.of(committerDate, committerTime, committerZoneId);

        RevCommit revCommit = testRepository.createCommit(commitBuilder -> {
            commitBuilder.setMessage("second commit message");
            commitBuilder.setAuthor(new Person("John Smith", "john.smith@acme.com"));
            commitBuilder.setAutorDateTime(authorDateTime);
            commitBuilder.setCommitter(new Person("Jane Smith", "jane.smith@acme.com"));
            commitBuilder.setCommitDateTime(committerDateTime);
        });

        Git git = testRepository.getGit();
        Iterable<RevCommit> revCommits = git.log().call();
        List<RevCommit> revCommitsList = StreamSupport.stream(revCommits.spliterator(), false).collect(Collectors.toList());
        assertEquals(2, revCommitsList.size());

        assertEquals("main", jGitInfo.getBranch());

        assertEquals(revCommit.getId().name(), jGitInfo.getCommitId());
        assertEquals(revCommit.getId().abbreviate(7).name(), jGitInfo.getShortCommitId());

        assertEquals("john.smith@acme.com", jGitInfo.getAuthorEmail());
        assertEquals("John Smith", jGitInfo.getAuthorName());
        assertEquals(authorDateTime, jGitInfo.getAuthorDateTime());

        assertEquals("jane.smith@acme.com", jGitInfo.getCommitterEmail());
        assertEquals("Jane Smith", jGitInfo.getCommitterName());
        assertEquals(committerDateTime, jGitInfo.getCommitDateTime());

        assertEquals("second commit message", jGitInfo.getCommitMessage());
    }

    @Test
    void tags() throws Exception {
        oneCommit();
        testRepository.createCommit("release v0.0.9", b -> {});
        testRepository.tag("v0.0.9");

        testRepository.createCommit("release v1.0.0", b -> {});
        testRepository.tag("v1.0.0");

        List<String> tags = jGitInfo.getTags();

        assertEquals(List.of("v1.0.0"), tags);
    }

    @Test
    void tagsAnnotated() throws Exception {
        oneCommit();

        testRepository.tag("v1.0.0");
        testRepository.tag("first_release", "Released on 28.03.24");

        List<String> tags = jGitInfo.getTags();

        assertEquals(List.of("first_release", "v1.0.0"), tags);
    }
}