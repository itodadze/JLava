package logger;

public enum LogMessages {
    FILE_PATH_NOT_FOUND("Non-existing file path"),
    FILE_COMPILATION_SUCCESS("Files compiled successfully"),
    FILE_COMPILATION_FAILURE("File compilation error"),
    HTTP_ERROR("Http error"),
    DEPENDENCY_NOT_FOUND("Could not download dependency"),
    DEPENDENCY_DOWNLOAD_SUCCESS("Dependency downloaded successfully"),
    JAR_FILE_PROCESS_ERROR("Could not process jar file"),
    JAR_FILE_CREATION_ERROR("Could not create jar file");

    private final String message;

    public String string() {
        return this.message;
    }

    LogMessages(String message) {
        this.message = message;
    }
}
