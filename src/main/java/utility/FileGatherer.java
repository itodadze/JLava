package utility;

import logger.LogMessages;
import logger.Logger;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

/**
 * A class responsible for recursively gathering files.
 */
public class FileGatherer {
    private final Logger logger;
    private final String fileType;

    /**
     * Constructs an instance of FileGatherer.
     *
     * @param logger    for logging messages and errors.
     * @param fileType  the type of files the class should gather.
     */
    public FileGatherer(Logger logger, String fileType) {
        this.logger = logger;
        this.fileType = fileType;
    }

    /**
     * Searches and returns the paths of files in provided source files (can be
     * directory or a file).
     *
     * @param sourceFiles   paths to the source files.
     * @return              the paths of all the files in source files of the
     *                      desired type.
     */
    public List<String> filesFromSources(List<String> sourceFiles) {
        return sourceFiles.stream()
                          .map(File::new)
                          .flatMap(this::filesIn)
                          .toList();
    }

    private Stream<String> filesIn(File file) {
        if (!file.exists()) {
            this.logger.printLine(LogMessages.FILE_PATH_NOT_FOUND.string() + ": %s", file.getName());
        } else {
            if (file.isDirectory()) {
                return Stream.ofNullable(file.listFiles())
                             .flatMap(Stream::of)
                             .flatMap(this::filesIn);
            } else if (file.isFile() && file.getName().endsWith("." + fileType)) {
                return Stream.of(file.getPath());
            }
        }
        return Stream.empty();
    }

}
