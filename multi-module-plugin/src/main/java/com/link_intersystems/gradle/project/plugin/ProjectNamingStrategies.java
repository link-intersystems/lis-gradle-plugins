package com.link_intersystems.gradle.project.plugin;

import java.nio.file.Path;

public enum ProjectNamingStrategies implements ProjectNamingStrategy {

    /**
     * Resolves to project names where the path hierarchy is converted to a gradle project hierarchy that
     * is separated by ':'. E.g. 'modules/adapter/api' will be converted to 'modules:adapter:api'.
     */
    HIERARCHICAL_ROOTLESS {
        @Override
        public String getProjectName(Path rootDirectory, Path rootRelativeProjectPath) {
            return ProjectNamingStrategies.convertPath(rootRelativeProjectPath, PROJECT_PATH_DELIM);
        }

        @Override
        public String toString() {
            return "HIERARCHICAL_ROOTLESS";
        }
    },
    /**
     * The same as {@link #HIERARCHICAL_ROOTLESS}, but includes the rootDirectory. E.g.
     * 'rootDir/modules/adapter/api' will be converted to 'rootDir:modules:adapter:api'.
     */
    HIERARCHICAL {
        @Override
        public String getProjectName(Path rootDirectory, Path rootRelativeProjectPath) {
            Path rootedProjectPath = rootDirectory.resolve(rootRelativeProjectPath);
            Path parentPath = rootDirectory.getParent();
            if (parentPath != null) {
                rootedProjectPath = parentPath.relativize(rootedProjectPath);
            }
            return ProjectNamingStrategies.convertPath(rootedProjectPath, PROJECT_PATH_DELIM);
        }

        @Override
        public String toString() {
            return "HIERARCHICAL";
        }
    },
    /**
     * Resolves to project names where the path hierarchy is converted to a '-' separated string.
     * E.g. 'modules/adapter/api' will be converted to 'modules-adapter-api'.
     */
    FLAT_ROOTLESS {
        @Override
        public String getProjectName(Path rootDirectory, Path rootRelativeProjectPath) {
            return ProjectNamingStrategies.convertPath(rootRelativeProjectPath, "-");
        }

        @Override
        public String toString() {
            return "FLAT_ROOTLESS";
        }
    },
    /**
     * The same as {@link #FLAT_ROOTLESS}, but includes the rootDirectory. E.g.
     * 'rootDir/modules/adapter/api' will be converted to 'rootDir-modules-adapter-api'.
     */
    FLAT {
        @Override
        public String getProjectName(Path rootDirectory, Path rootRelativeProjectPath) {
            Path rootedProjectPath = rootDirectory.resolve(rootRelativeProjectPath);
            Path parentPath = rootDirectory.getParent();
            if (parentPath != null) {
                rootedProjectPath = parentPath.relativize(rootedProjectPath);
            }
            return ProjectNamingStrategies.convertPath(rootedProjectPath, "-");
        }

        @Override
        public String toString() {
            return "FLAT";
        }
    },
    /**
     * The simple {@link ProjectNamingStrategy} resolves project paths to their simple name, which is the
     * last path segment (the filename). This strategy can lead to ambiguous project names which might be omitted.
     * E.g. 'modules/adapter/api' will be converted to 'api'.
     */
    SIMPLE {
        @Override
        public String getProjectName(Path rootDirectory, Path rootRelativeProjectPath) {
            return ProjectNamingStrategies.convertPath(rootRelativeProjectPath.getFileName(), "-");
        }

        @Override
        public String toString() {
            return "SIMPLE";
        }
    };

    public static final String PROJECT_PATH_DELIM = ":";

    private static String convertPath(final Path path, final String delim) {
        StringBuilder projectNameBuilder = new StringBuilder();
        for (int i = 0; i < path.getNameCount(); i++) {
            String pathElement = path.getName(i).getFileName().toString();
            projectNameBuilder.append(pathElement);
            if (i < path.getNameCount() - 1) {
                projectNameBuilder.append(delim);
            }
        }
        return projectNameBuilder.toString();
    }
}
