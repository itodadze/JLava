package logger;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class OutputLogger implements Logger {

    private PrintWriter printWriter;

    public OutputLogger(OutputStream outputStream) {
        this.printWriter = new PrintWriter(new OutputStreamWriter(outputStream));
    }

    public void printLine(String format, Object... args) {
        this.printWriter.printf(format, args);
    }

    public void close() {
        if (this.printWriter != null) {
            this.printWriter.close();
            this.printWriter = null;
        }
    }

}
