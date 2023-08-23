package compiler;

import logger.Logger;
import utility.FileGatherer;

import java.util.ArrayList;
import java.util.List;

import static logger.LogMessages.FILE_COMPILATION_FAILURE;
import static logger.LogMessages.FILE_COMPILATION_SUCCESS;

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
                fileGatherer.filesFromSources(sourceFiles)
        );
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                this.logger.printLine(FILE_COMPILATION_SUCCESS.string());
            } else {
                this.logger.printLine(FILE_COMPILATION_FAILURE.string() + ": exit code: %d", exitCode);
            }
        } catch (Exception e) {
            this.logger.printLine(FILE_COMPILATION_FAILURE.string() + ": %s", e.getMessage());
        }
    }

}
