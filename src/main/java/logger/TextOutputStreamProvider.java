package logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Provides an output stream from a text file.
 */
public class TextOutputStreamProvider implements OutputStreamProvider {
    private final String path;

    /**
     * Constructs an instance of TextOutputStreamProvider
     *
     * @param path  path to the text file.
     */
    public TextOutputStreamProvider(String path) {
        this.path = path;
    }
    @Override
    public OutputStream get() throws Exception {
        File file = new File(this.path);
        Exception e = new IllegalArgumentException(this.path);
        if (file.isDirectory()) throw e;
        if (file.isFile()) {
            if(!file.delete()) { throw e; }
        }
        if (!file.createNewFile()) {
            throw e;
        }
        return new FileOutputStream(file);
    }
}
