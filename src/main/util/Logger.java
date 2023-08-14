package main.util;

public interface Logger {

    String FILE_PATH_NOT_FOUND = "Non-existing file path";
    String NULL_FILE_ACCESS = "Null file accessed";

    void printLine(String format, Object... args);
    void close();
}
