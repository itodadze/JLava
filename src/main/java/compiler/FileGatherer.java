package compiler;

import util.LogMessages;
import util.Logger;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
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
                .collect(Collectors.toList());
    }

    private Stream<String> javaFilesIn(File file) {
        if (file == null) {
            this.logger.printLine(LogMessages.NULL_FILE_ACCESS);
        } else if (!file.exists()) {
            this.logger.printLine(LogMessages.FILE_PATH_NOT_FOUND + ": %s", file.getName());
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
