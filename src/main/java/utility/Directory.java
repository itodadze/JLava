package utility;

import java.io.File;
import java.io.IOException;

import static logger.LogMessages.FILE_CREATION_ERROR;
import static logger.LogMessages.FILE_DELETION_ERROR;

/**
 * A class responsible for recursively clearing out the content in directory.
 */
public class Directory {
    private final File directory;

    /**
     * Constructs an instance of the ClearableDirectory.
     *
     * @param directory the directory to be cleared.
     */
    public Directory(File directory) {
        this.directory = directory;
    }

    public void delete() throws Exception {
        clearContent();
        if (!this.directory.delete()) {
            throw new IOException(FILE_DELETION_ERROR.string() + ": " + this.directory);
        }
    }

    public void clearContent() throws Exception {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    (new Directory(file)).delete();
                }
            }
        }
    }

    public void make() throws Exception {
        if (!this.directory.mkdir()) {
            throw new IOException(FILE_CREATION_ERROR.string() + ": " + this.directory);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void makeIfNotExists() {
        this.directory.mkdir();
    }
}
