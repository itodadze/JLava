package main.compiler;

import main.util.LogMessages;
import main.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class ClassCompiler {

    private final Logger logger;
    private final FileGatherer fileGatherer;

    public ClassCompiler(Logger logger, FileGatherer fileGatherer) {
        this.logger = logger;
        this.fileGatherer = fileGatherer;
    }

    public void compileClasses(List<String> sourceFiles, String outputDirectory) {
        List<String> command = new ArrayList<>();
        command.add("javac");
        command.add("-d");
        command.add(outputDirectory);
        command.addAll(
                fileGatherer.javaFilesFromSources(sourceFiles)
        );
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                this.logger.printLine(LogMessages.FILE_COMPILATION_SUCCESS);
            } else {
                this.logger.printLine(LogMessages.FILE_COMPILATION_FAILURE + ": exit code: %d", exitCode);
            }
        } catch (Exception e) {
            this.logger.printLine(LogMessages.FILE_COMPILATION_FAILURE + ": %s", e.getMessage());
        }
    }

}
