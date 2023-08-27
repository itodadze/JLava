package logger;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * A class responsible for logging messages to the chosen output.
 */
public class OutputLogger implements Logger {
    private PrintWriter printWriter;

    /**
     * Constructs an instance of the OutputLogger.
     *
     * @param outputStream  to which the logs are sent to.
     */
    public OutputLogger(OutputStream outputStream) {
        this.printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
    }

    public synchronized void printLine(String format, Object... args) {
        this.printWriter.printf(format, args);
    }

    /**
     * Closes the output stream.
     */
    public synchronized void close() {
        if (this.printWriter != null) {
            this.printWriter.close();
            this.printWriter = null;
        }
    }

}
