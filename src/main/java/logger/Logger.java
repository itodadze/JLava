package logger;

public interface Logger {

    /**
     * Logs the message.
     *
     * @param format    the message.
     * @param args      the arguments.
     */
    void printLine(String format, Object... args);

    /**
     * Closes the logger if it needs to be closed.
     */
    void close();
}
