package logger;

public enum LogMessages {
    FILE_PATH_NOT_FOUND("Non-existing file path"),
    FILE_COMPILATION_SUCCESS("Files compiled successfully"),
    FILE_COMPILATION_FAILURE("File compilation error"),
    HTTP_ERROR("Http error"),
    DEPENDENCY_NOT_FOUND("Could not download dependency"),
    DEPENDENCY_DOWNLOAD_SUCCESS("Dependency downloaded successfully");

    private final String message;

    public String string() {
        return this.message;
    }

    LogMessages(String message) {
        this.message = message;
    }
}
