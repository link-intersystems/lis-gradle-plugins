package com.link_intersystems.git.test;

import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TagCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.util.FS;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TestRepository {

    public static TestRepository clone(File repoDir, File remoteRepoDir)
            throws Exception {

        TestRepository testRepository = new TestRepository(repoDir);

        testRepository.getGit().remoteAdd().setName("origin").setUri(new URIish(remoteRepoDir.toURI().toURL())).call();
        testRepository.getGit().fetch().setRemote("origin").setRefSpecs("+refs/heads/*:refs/remotes/origin/*").call();
        Repository repository = testRepository.getGit().getRepository();
        repository.close();
        return new TestRepository(repoDir);
    }

    public void checkout(String revName)
            throws Exception {

        if (localBranchExists(revName)) {
            git.checkout().setName(revName).call();
        } else {
            String branchName = revName;
            if (branchName.startsWith("origin/")) {
                branchName = branchName.substring("origin/".length());
            }
            git.checkout().setCreateBranch(true).setStartPoint(revName).setName(branchName).call();
        }

    }

    private boolean localBranchExists(String revName) throws GitAPIException {
        List<Ref> localRefs = git.branchList().call();
        for (Ref localRef : localRefs) {
            Ref leafRef = localRef.getLeaf();
            if (leafRef.getName().equals("refs/heads/" + revName)) {
                return true;
            }
        }

        return false;
    }

    public void tag(String tag)
            throws Exception {

        tag(tag, null);
    }

    public void tag(String tag, String message)
            throws Exception {

        Ref ref = git.getRepository().findRef(Constants.HEAD);
        RevWalk revCommits = new RevWalk(getGit().getRepository());

        RevCommit revCommit = revCommits.parseCommit(ref.getObjectId());
        tag(revCommit, tag, message);
    }

    public void tag(RevObject objectId, String tag)
            throws Exception {

        tag(objectId, tag, null);
    }

    public void tag(RevObject objectId, String tag, String message)
            throws Exception {

        TagCommand tagCommand = git.tag().setObjectId(objectId).setName(tag);

        tagCommand.setAnnotated(message != null);
        tagCommand.setMessage(message);

        tagCommand.call();
    }

    public void close() {
        getGit().getRepository().close();
    }

    private final Git git;
    private File basedir;

    private Person author = new Person("John Doe", "john.doe@acme.com");
    private Person committer = new Person("John Doe", "john.doe@acme.com");

    public TestRepository(Path basedir) throws Exception {
        this(basedir.toFile());

    }

    public TestRepository(File basedir) throws Exception {

        this.basedir = basedir;
        if (RepositoryCache.FileKey.isGitRepository(basedir, FS.DETECTED)) {
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder.setGitDir(basedir).readEnvironment() // scan environment GIT_* variables
                    .findGitDir() // scan up the file system tree
                    .build();
            git = new Git(repository);
        } else {
            git = Git.init().setDirectory(basedir).setInitialBranch("main").call();
        }
    }


    public Git getGit() {

        return git;
    }

    private void addFile(String path, String content)
            throws GitAPIException, IOException {

        addFile(path, new ByteArrayInputStream(content.getBytes(UTF_8)));
    }

    private File addFile(String path, InputStream content)
            throws IOException, GitAPIException {

        File file = new File(basedir, path);

        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            byte[] buff = new byte[8192];
            int read = 0;
            while ((read = content.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        }

        git.add().addFilepattern(path).call();

        return file;
    }

    public File addFile(String path)
            throws IOException, GitAPIException {

        return addFile(Paths.get(path));
    }

    public File addFile(Path file)
            throws IOException, GitAPIException {

        if (!Files.exists(file)) {
            Files.createFile(file);
        }

        git.add().addFilepattern(file.toString()).call();

        return file.toFile();
    }

    public void addAllFiles() throws IOException {
        addAllFiles(f -> true);
    }

    public void addAllFiles(Predicate<Path> filePredicate) throws IOException {
        Files.walkFileTree(basedir.toPath(), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (filePredicate.test(file)) {
                    try {
                        addFile(file);
                    } catch (GitAPIException e) {
                        throw new IOException(e);
                    }
                }
                return super.visitFile(file, attrs);
            }
        });
    }

    public String currentBranch()
            throws IOException {

        return git.getRepository().getBranch();
    }

    private CommitContentBuilder getCommitContentBuilder() {
        return new CommitContentBuilder() {

            @Override
            public void addFile(String path, String content) {

                try {
                    TestRepository.this.addFile(path, content);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void addAllFiles() {
                try {
                    TestRepository.this.addAllFiles();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void addFile(String path) {
                try {
                    TestRepository.this.addFile(path);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }


    public RevCommit createCommit(Consumer<CommitBuilder> commitBuilderConsumer) {
        return createCommit(author, commitBuilderConsumer);
    }

    public RevCommit createCommit(Person author, Consumer<CommitBuilder> commitBuilderConsumer) {
        JGitCommitDescription jGitCommitDescription = new JGitCommitDescription(author, author, getCommitContentBuilder());
        commitBuilderConsumer.accept(jGitCommitDescription);

        CommitCommand commit = git.commit();

        PersonIdent authorIdent = jGitCommitDescription.getAuthorIdent();
        commit.setAuthor(authorIdent);

        PersonIdent committerIdent = jGitCommitDescription.getCommitterIdent();
        commit.setCommitter(committerIdent);

        String message = jGitCommitDescription.getMessage();
        commit.setMessage(message);
        commit.setAllowEmpty(true);

        try {
            return commit.call();
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }

    }

    public RevCommit createCommit(String message, Consumer<CommitContentBuilder> commitContentBuilder)
            throws Exception {

        return createCommit(author, builder -> {
            builder.setMessage(message);
            builder.withContent(commitContentBuilder);
        });

    }

    public void newBranch(String branchName)
            throws Exception {

        git.checkout().setCreateBranch(true).setName(branchName).call();
    }
}
