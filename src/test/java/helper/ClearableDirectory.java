package helper;

import java.io.File;

public class ClearableDirectory {

    private final File directory;

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
