package logger;

/**
 * Provides custom log messages for the jlava.
 */
public enum LogMessages {
    BUILD_ERROR("Could not build"),
    CONFIG_FILE_ERROR("Config file error"),
    CONFIG_NOT_FOUND("Could not find config file"),
    DEPENDENCY_DOWNLOAD_SUCCESS("Dependency downloaded successfully"),
    DEPENDENCIES_FETCH_FAILURE("Dependencies could not be fetched"),
    DEPENDENCIES_FETCH_SUCCESS("All dependencies fetched"),
    DEPENDENCY_NOT_FOUND("Could not download dependency"),
    FILE_COMPILATION_FAILURE("File compilation error"),
    FILE_COMPILATION_SUCCESS("Files compiled successfully"),
    FILE_CREATION_ERROR("Could not create file"),
    FILE_DELETION_ERROR("Could not delete file"),
    FILE_PATH_NOT_FOUND("Non-existing file path"),
    HTTP_ERROR("Http error"),
    JAR_FILE_CREATION_ERROR("Could not create jar file"),
    JSON_FILE_ERROR("Could not parse json file"),
    JAR_FILE_PROCESS_ERROR("Could not process jar file"),
    PACKAGING_ERROR("Could not package compiled classes"),
    PACKAGING_SUCCESS("Packaged compiled classes successfully");

    private final String message;

    public String string() {
        return this.message;
    }

    LogMessages(String message) {
        this.message = message;
    }
}
