package main.java.util;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Logger {

    private PrintWriter printWriter;

    public Logger(OutputStream outputStream) {
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
