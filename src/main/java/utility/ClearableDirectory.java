package utility;

import java.io.File;

/**
 * A class responsible for recursively clearing out the content in directory.
 */
public class ClearableDirectory {
    private final File directory;

    /**
     * Constructs an instance of the ClearableDirectory.
     *
     * @param directory the directory to be cleared.
     */
    public ClearableDirectory(File directory) {
        this.directory = directory;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void clearContent() {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        (new ClearableDirectory(file)).clearContent();
                    }
                    file.delete();
                }
            } else {
                directory.delete();
            }
        }
    }
}
