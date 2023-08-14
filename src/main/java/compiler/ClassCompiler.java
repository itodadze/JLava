package main.java.compiler;

import main.java.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class ClassCompiler {

    private final Logger logger;

    public ClassCompiler(Logger logger) {
        this.logger = logger;
    }

    public void compileClasses(List<String> sourceFiles, String outputDirectory) {
        List<String> command = new ArrayList<>();
        command.add("javac");
        command.add("-d");
        command.add(outputDirectory);
        command.addAll(sourceFiles);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                this.logger.printLine("Files compiled successfully");
            } else {
                this.logger.printLine("Compilation error, exit code: %d", exitCode);
            }
        } catch (Exception e) {
            this.logger.printLine("Compilation error: %s", e.getMessage());
        }
    }

}
