package compiler;

import logger.LogMessages;
import logger.Logger;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

public class FileGatherer {

    private final Logger logger;

    public FileGatherer(Logger logger) {
        this.logger = logger;
    }

    public List<String> javaFilesFromSources(List<String> sourceFiles) {
        return sourceFiles.stream()
                          .map(File::new)
                          .flatMap(this::javaFilesIn)
                          .toList();
    }

    private Stream<String> javaFilesIn(File file) {
        if (!file.exists()) {
            this.logger.printLine(LogMessages.FILE_PATH_NOT_FOUND.string() + ": %s", file.getName());
        } else {
            if (file.isDirectory()) {
                return Stream.ofNullable(file.listFiles())
                             .flatMap(Stream::of)
                             .flatMap(this::javaFilesIn);
            } else if (file.isFile() && file.getName().endsWith(".java")) {
                return Stream.of(file.getPath());
            }
        }
        return Stream.empty();
    }

}
