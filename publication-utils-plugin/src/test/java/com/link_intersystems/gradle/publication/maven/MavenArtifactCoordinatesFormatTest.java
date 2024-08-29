package com.link_intersystems.gradle.publication.maven;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MavenArtifactCoordinatesFormatTest {

    private MavenArtifactCoordinatesFormat format;

    @BeforeEach
    void setUp() {
        format = new MavenArtifactCoordinatesFormat();
    }

    @Test
    void formatAndParse() throws ParseException {
        MavenArtifactCoordinates artifact = new MavenArtifactCoordinates("groupId", "artifactId", "version", "extension", "classifier");
        MavenArtifactCoordinates parsedArtifact = format.parse(format.format(artifact));
        assertEquals(artifact, parsedArtifact);
    }

    @Test
    void formatAndParseWithoutClassifier() throws ParseException {
        MavenArtifactCoordinates artifact = new MavenArtifactCoordinates("groupId", "artifactId", "version", "extension");
        MavenArtifactCoordinates parsedArtifact = format.parse(format.format(artifact));
        assertEquals(artifact, parsedArtifact);
    }

    @Test
    void format() {
        MavenArtifactCoordinates artifact = new MavenArtifactCoordinates("groupId", "artifactId", "version", "extension", "classifier");
        assertEquals("groupId:artifactId:version:extension:classifier", format.format(artifact));
    }

    @Test
    void formatWithoutQualifier() {
        MavenArtifactCoordinates artifact = new MavenArtifactCoordinates("groupId", "artifactId", "version", "extension");

        String formatted = new MavenArtifactCoordinatesFormat().format(artifact);
        assertEquals("groupId:artifactId:version:extension", formatted);
    }

    @Test
    void parse() throws ParseException {
        MavenArtifactCoordinates parsedArtifact = format.parse("groupId:artifactId:version:extension:classifier");
        MavenArtifactCoordinates artifact = new MavenArtifactCoordinates("groupId", "artifactId", "version", "extension", "classifier");
        assertEquals(artifact, parsedArtifact);
    }

    @Test
    void parseTooManyParts() {
        ParseException parseException = assertThrows(ParseException.class, () -> format.parse("groupId:artifactId:version:extension:classifier:wtf"));
        assertEquals("groupId:artifactId:version:extension:classifier:".length(), parseException.getErrorOffset());
        assertEquals("unknown part", parseException.getLocalizedMessage());
    }

    @Test
    void parseWithoutClassifier() throws ParseException {
        MavenArtifactCoordinates parsedArtifact = format.parse("groupId:artifactId:version:extension");
        MavenArtifactCoordinates artifact = new MavenArtifactCoordinates("groupId", "artifactId", "version", "extension");
        assertEquals(artifact, parsedArtifact);
    }

    @Test
    void parseEmpty() {
        ParseException parseException = assertThrows(ParseException.class, () -> format.parse(""));
        assertEquals(0, parseException.getErrorOffset());
        assertEquals("Missing groupId", parseException.getLocalizedMessage());
    }

    @Test
    void parseMissingArtifactId() {
        ParseException parseException = assertThrows(ParseException.class, () -> format.parse("groupId"));
        assertEquals("groupId".length(), parseException.getErrorOffset());
        assertEquals("Missing artifactId", parseException.getLocalizedMessage());
    }

    @Test
    void parseMissingArtifactIdWithSeparator() {
        ParseException parseException = assertThrows(ParseException.class, () -> format.parse("groupId:"));
        assertEquals("groupId:".length(), parseException.getErrorOffset());
        assertEquals("Missing artifactId", parseException.getLocalizedMessage());
    }

    @Test
    void parseMissingVersion() {
        ParseException parseException = assertThrows(ParseException.class, () -> format.parse("groupId:artifactId"));
        assertEquals("groupId:artifactId".length(), parseException.getErrorOffset());
        assertEquals("Missing version", parseException.getLocalizedMessage());
    }

    @Test
    void parseMissingVersionWithSeparator() {
        ParseException parseException = assertThrows(ParseException.class, () -> format.parse("groupId:artifactId:"));
        assertEquals("groupId:artifactId:".length(), parseException.getErrorOffset());
        assertEquals("Missing version", parseException.getLocalizedMessage());
    }

    @Test
    void parseMissingExtension() {
        ParseException parseException = assertThrows(ParseException.class, () -> format.parse("groupId:artifactId:version"));
        assertEquals("groupId:artifactId:version".length(), parseException.getErrorOffset());
        assertEquals("Missing extension", parseException.getLocalizedMessage());
    }

    @Test
    void parseMissingExtensionWithSeparator() {
        ParseException parseException = assertThrows(ParseException.class, () -> format.parse("groupId:artifactId:version:"));
        assertEquals("groupId:artifactId:version:".length(), parseException.getErrorOffset());
        assertEquals("Missing extension", parseException.getLocalizedMessage());
    }

}