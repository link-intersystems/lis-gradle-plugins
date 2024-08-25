package com.link_intersystems.gradle.publication.maven;

import org.jetbrains.annotations.NotNull;

import java.text.*;

public class MavenArtifactCoordinatesFormat extends Format {

    public static String formatMavenArtifact(MavenArtifactCoordinates mavenArtifactCoordinates) {
        return new MavenArtifactCoordinatesFormat().format(mavenArtifactCoordinates);
    }

    public final String format(MavenArtifactCoordinates mavenArtifact) {
        return format((Object) mavenArtifact, new StringBuffer(), new FieldPosition(0)).toString();
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        if (obj instanceof MavenArtifactCoordinates) {
            return format((MavenArtifactCoordinates) obj, toAppendTo, pos);
        } else {
            throw new IllegalArgumentException("Cannot format given Object as a MavenArtifact.");
        }
    }

    public StringBuffer format(MavenArtifactCoordinates mavenArtifact, StringBuffer toAppendTo, FieldPosition pos) {
        toAppendTo = toAppendTo.append(mavenArtifact.getGroupId()).append(':').append(mavenArtifact.getArtifactId()).append(':').append(mavenArtifact.getVersion()).append(':').append(mavenArtifact.getExtension());

        if (mavenArtifact.getClassifier() != null) {
            toAppendTo.append(':').append(mavenArtifact.getClassifier());
        }

        return toAppendTo;
    }

    @Override
    public Object parseObject(String source, @NotNull ParsePosition pos) {
        return null;
    }

    public MavenArtifactCoordinates parse(String source) throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        return parse(source, pos);
    }

    public MavenArtifactCoordinates parse(String text, ParsePosition pos) throws ParseException {
        int textLength = text.length();

        StringBuilder partBuilder = new StringBuilder();

        ArtifactCoordinates cords = new ArtifactCoordinates();

        for (; pos.getIndex() < textLength; pos.setIndex(pos.getIndex() + 1)) {
            if (cords.isComplete()) {
                pos.setErrorIndex(pos.getIndex());
                break;
            }

            char charAt = text.charAt(pos.getIndex());

            if (charAt == ':') {
                cords.add(partBuilder);
                partBuilder = new StringBuilder();
            } else if (Character.isWhitespace(charAt)) {
                pos.setErrorIndex(pos.getIndex());
            } else {
                partBuilder.append(charAt);
            }
        }

        if (partBuilder.length() > 0) {
            cords.add(partBuilder);
        }

        if (!cords.isValid()) {
            pos.setErrorIndex(pos.getIndex());
        }

        if (pos.getErrorIndex() != -1) {
            String message = cords.nextPartName();
            ;
            throw new ParseException(message, pos.getErrorIndex());
        }

        return cords.build();
    }

    private static class ArtifactCoordinates {
        private String[] partNames = new String[]{"groupId", "artifactId", "version", "extension", "classifier"};
        private String[] parts = new String[5];
        private int partsIndex = 0;

        public void add(CharSequence part) {
            parts[partsIndex++] = part.toString();
        }

        public boolean isComplete() {
            return partsIndex == parts.length;
        }

        public boolean isValid() {
            return partsIndex > 3;
        }

        public String nextPartName() {
            return partsIndex < parts.length ? partNames[partsIndex] : "unknown part";
        }

        public MavenArtifactCoordinates build() {
            return new MavenArtifactCoordinates(parts[0], parts[1], parts[2], parts[3], parts[4]);
        }
    }
}