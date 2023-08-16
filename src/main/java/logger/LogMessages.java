package logger;

public enum LogMessages {
    FILE_PATH_NOT_FOUND("Non-existing file path"),
    FILE_COMPILATION_SUCCESS("Files compiled successfully"),
    FILE_COMPILATION_FAILURE("File compilation error");

    private final String message;

    public String string() {
        return this.message;
    }

    LogMessages(String message) {
        this.message = message;
    }
}
