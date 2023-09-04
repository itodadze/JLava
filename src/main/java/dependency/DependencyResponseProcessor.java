package dependency;

import logger.Logger;
import org.apache.http.client.methods.CloseableHttpResponse;
import utility.Directory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;

import static logger.LogMessages.JAR_FILE_CREATION_ERROR;
import static logger.LogMessages.JAR_FILE_PROCESS_ERROR;
import static utility.Namer.DIRECTORY;
import static utility.Namer.JAR;

/**
 * A class responsible for processing the http response when dependency is found.
 */
public class DependencyResponseProcessor {
    private final Logger logger;
    private final String dependencyDirectory;

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
    public String process(CloseableHttpResponse response, String repository, String name) throws Exception {
        try {
            InputStream content = response.getEntity().getContent();
            new Directory(Paths.get(this.dependencyDirectory, DIRECTORY.name(repository))
                    .toFile()).makeIfNotExists();
            File jarFile = Paths.get(this.dependencyDirectory, DIRECTORY.name(repository),
                    JAR.name(name)).toFile();
            if (!jarFile.createNewFile()) {
                this.logger.printLine(JAR_FILE_CREATION_ERROR.string() + ": %s", name);
            } else {
                saveInFile(jarFile, content);
            }
            return jarFile.getPath();
        } catch (Exception e) {
            this.logger.printLine(JAR_FILE_PROCESS_ERROR.string() + ": %s", e.getMessage());
            throw e;
        }
    }

    private void saveInFile(File file, InputStream content) throws Exception {
        try (OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = content.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }
}
