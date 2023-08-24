package logger;

public enum LogMessages {
    FILE_PATH_NOT_FOUND("Non-existing file path"),
    FILE_COMPILATION_SUCCESS("Files compiled successfully"),
    FILE_COMPILATION_FAILURE("File compilation error"),
    HTTP_ERROR("Http error"),
    DEPENDENCY_NOT_FOUND("Could not download dependency"),
    DEPENDENCY_DOWNLOAD_SUCCESS("Dependency downloaded successfully"),
    JAR_FILE_PROCESS_ERROR("Could not process jar file"),
    JAR_FILE_CREATION_ERROR("Could not create jar file"),
    DEPENDENCIES_FETCH_SUCCESS("All dependencies fetched"),
    DEPENDENCIES_FETCH_FAILURE("Dependencies could not be fetched"),
    CONFIG_NOT_FOUND("Could not find config file"),
    JSON_FILE_ERROR("Could not parse json file"),
    CONFIG_FILE_ERROR("Config file error"),
    BUILD_ERROR("Could not build");

    private final String message;

    public String string() {
        return this.message;
    }

    LogMessages(String message) {
        this.message = message;
    }
}
