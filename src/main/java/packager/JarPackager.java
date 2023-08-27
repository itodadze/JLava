package packager;

import logger.Logger;

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
     * @param logger    for logging messages and erors.
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
                this.logger.printLine(PACKAGING_SUCCESS.string());
                if (removeClassesOutsideJar(source) != 0) {
                    this.logger.printLine(PACKAGING_ERROR.string() + ": exit code: %d", exitCode);
                }
            } else {
                this.logger.printLine(PACKAGING_ERROR.string() + ": exit code: %d", exitCode);
            }
        } catch (Exception e) {
            this.logger.printLine(PACKAGING_ERROR.string() + ": %s", e.getMessage());
            throw e;
        }
    }

    private int removeClassesOutsideJar(String directory) throws Exception {
        List<String> command = List.of("find", directory, "-name",
                "*.class", "-type", "f", "-delete");
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();
        return process.waitFor();
    }
}
