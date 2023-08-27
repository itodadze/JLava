package logger;

import java.io.OutputStream;

public interface OutputStreamProvider {
    /**
     * @return the output stream.
     * @throws Exception if any error occurred.
     */
    OutputStream get() throws Exception;
}
