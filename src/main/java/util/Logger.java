package util;

public interface Logger {
    void printLine(String format, Object... args);
    void close();
}
