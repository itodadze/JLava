package dependency;

import logger.Logger;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static logger.LogMessages.JAR_FILE_CREATION_ERROR;
import static logger.LogMessages.JAR_FILE_PROCESS_ERROR;

public class DependencyResponseProcessor {
    private final Logger logger;
    private final String dependencyDirectory;
    public DependencyResponseProcessor(Logger logger, String dependencyDirectory) {
        this.logger = logger;
        this.dependencyDirectory = dependencyDirectory;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public String process(CloseableHttpResponse response, String repository, String name) throws Exception {
        try {
            InputStream content = response.getEntity().getContent();
            File repositoryDirectory = new File(this.dependencyDirectory + "/" + repository);
            if (!repositoryDirectory.exists()) repositoryDirectory.mkdir();
            File jarFile = new File(this.dependencyDirectory + "/" + repository
                    + "/" + name + ".jar");
            if (!jarFile.createNewFile()) {
                this.logger.printLine(JAR_FILE_CREATION_ERROR.string() + ": %s", name);
            } else {
                try (OutputStream outputStream = new FileOutputStream(jarFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = content.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }
            return jarFile.getPath();
        } catch(Exception e) {
            this.logger.printLine(JAR_FILE_PROCESS_ERROR.string() + ": %s", e.getMessage());
            throw e;
        }
    }

}
