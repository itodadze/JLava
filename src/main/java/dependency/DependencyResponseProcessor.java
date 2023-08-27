package dependency;

import logger.Logger;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;

import static logger.LogMessages.JAR_FILE_CREATION_ERROR;
import static logger.LogMessages.JAR_FILE_PROCESS_ERROR;

/**
 * A class responsible for processing the http response when dependency is found.
 */
public class DependencyResponseProcessor {
    private final Logger logger;
    private final String dependencyDirectory;

    /**
     * Constructs an instance of DependencyResponseProcessor.
     *
     * @param logger                for logging messages and errors.
     * @param dependencyDirectory   path to where dependencies are saved.
     */
    public DependencyResponseProcessor(Logger logger, String dependencyDirectory) {
        this.logger = logger;
        this.dependencyDirectory = dependencyDirectory;
    }

    /**
     * Processes the response and returns the path to where the dependency is saved.
     *
     * @param response      the http response which is processed.
     * @param repository    the repository in which it was found.
     * @param name          the name of the dependency.
     * @return              the path of the saved dependency.
     * @throws Exception    If the processing is unsuccessful.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public String process(CloseableHttpResponse response, String repository, String name) throws Exception {
        try {
            InputStream content = response.getEntity().getContent();
            File repositoryDirectory = new File(Paths.get(this.dependencyDirectory, repository).toString());
            if (!repositoryDirectory.exists()) repositoryDirectory.mkdir();
            File jarFile = new File(Paths.get(this.dependencyDirectory, repository,
                    name + ".jar").toString());
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
