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

    /**
     * Constructs an instance of ClassCompiler.
     *
     * @param logger        for logging messages and errors.
     * @param fileGatherer  for searching java files recursively in source files.
     */
    public ClassCompiler(Logger logger, FileGatherer fileGatherer) {
        this.logger = logger;
        this.fileGatherer = fileGatherer;
    }

    /**
     * Finds and compiles the java files.
     *
     * @param sourceFiles       java files which are to be compiled, or directories
     *                          in which java files are found recursively and compiled.
     * @param dependencies      paths to dependencies, needed to compile the java files.
     * @param outputDirectory   directory in which the compiled class files are to be
     *                          stored.
     */
    public void compileClasses(List<String> sourceFiles, List<String> dependencies,
                               String outputDirectory) {
        List<String> command = new ArrayList<>();
        command.add("javac");
        if (!dependencies.isEmpty()) {
            command.add("-cp");
            command.add(String.join(":", dependencies));
        }
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
