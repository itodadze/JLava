package utility;

import logger.LogMessages;
import logger.Logger;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

public class FileGatherer {
    private final Logger logger;
    private final String fileType;

    public FileGatherer(Logger logger, String fileType) {
        this.logger = logger;
        this.fileType = fileType;
    }

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
