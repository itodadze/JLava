package logger;

import java.io.OutputStream;

public interface OutputStreamProvider {
    OutputStream get() throws Exception;
}
