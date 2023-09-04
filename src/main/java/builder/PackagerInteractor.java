package builder;

import config.Configuration;
import packager.Packager;
import utility.Directory;

import java.io.File;

import static config.ConfigurationKey.NAME;
import static config.ConfigurationKey.OUTPUT;

public class PackagerInteractor {
    private final Packager packager;

    public PackagerInteractor(Packager packager) {
        this.packager = packager;
    }

    /**
     * @param tempDirectory path to the directory in which compiled class files are
     *                      stored (directory is cleared after packaging).
     * @throws Exception    if any IO exception occurs or packaging fails.
     */
    public void packageClasses(Configuration configuration, File tempDirectory) throws Exception {
        this.packager.packageClasses(configuration.getString(NAME.key()),
                tempDirectory.getPath(), configuration.getString(OUTPUT.key()));
        (new Directory(tempDirectory)).delete();
    }

}
