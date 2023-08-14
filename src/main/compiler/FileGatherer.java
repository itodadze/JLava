package main.compiler;

import main.util.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileGatherer {

    private final Logger logger;

    public FileGatherer(Logger logger) {
        this.logger = logger;
    }

    public List<String> javaFilesFromSources(List<String> sourceFiles) {
        List<String> javaFiles = new ArrayList<>();
        for (String sourceFile: sourceFiles) {
            javaFiles.addAll(javaFilesIn(new File(sourceFile)));
        }
        return javaFiles;
    }

    private List<String> javaFilesIn(File file) {
        List<String> javaFiles = new ArrayList<>();
        if (file != null) {
            if (file.isDirectory()) {
                File[] childFiles = file.listFiles();
                if (childFiles != null) {
                    for (File childFile : childFiles) {
                        javaFiles.addAll(javaFilesIn(childFile));
                    }
                }
            } else if (file.isFile() && file.getName().endsWith(".java")) {
                javaFiles.add(file.getPath());
            } else if (!file.isFile()) {
                this.logger.printLine(Logger.FILE_PATH_NOT_FOUND + ": %s", file.getName());
            }
        } else {
            this.logger.printLine(Logger.NULL_FILE_ACCESS);
        }
        return javaFiles;
    }

}
