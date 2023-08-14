package helper;

import main.util.Logger;

public class StringLogger implements Logger {
    private String log;
    public StringLogger() {
        this.log = "";
    }
    public void printLine(String format, Object... args) {
        if (!this.log.isEmpty()) {
            this.log += "\n";
        }
        this.log += String.format(format, args) + "\n";
    }
    public void close() {
    }
    public String getLog() {
        return this.log;
    }
}
