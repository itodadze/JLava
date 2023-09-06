package packager;

import logger.Logger;
import utility.Directory;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import static logger.LogMessages.PACKAGING_ERROR;
import static logger.LogMessages.PACKAGING_SUCCESS;

/**
 * A class responsible for packaging class files into a jar file.
 */
public class JarPackager implements  Packager{
    private final Logger logger;

    /**
     * Constructs an instance of JarPackager.
     *
     * @param logger    for logging messages and errors.
     */
    public JarPackager(Logger logger) {
        this.logger = logger;
    }
    @Override
    public void packageClasses(String name, String source, String directory)
        throws Exception {
        List<String> command = List.of("jar", "cvf",
                Paths.get(directory, name + ".jar").toString(),
                "-C", Paths.get(source).toString(), ".");
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                removeClassesOutsideJar(source);
                this.logger.printLine(PACKAGING_SUCCESS.string());
            } else {
                this.logger.printLine(PACKAGING_ERROR.string() + ": exit code: %d", exitCode);
            }
        } catch (Exception e) {
            this.logger.printLine(PACKAGING_ERROR.string() + ": %s", e.getMessage());
            throw e;
        }
    }

    private void removeClassesOutsideJar(String directory) throws Exception {
        (new Directory(new File(directory))).clearContent();
    }
}
