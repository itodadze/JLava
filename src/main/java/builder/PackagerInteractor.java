package builder;

import config.Configuration;
import packager.Packager;
import utility.ClearableDirectory;

import java.io.File;

import static config.ConfigurationKey.NAME;
import static config.ConfigurationKey.OUTPUT;

public class PackagerInteractor {

    private final Packager packager;

    public PackagerInteractor(Packager packager) {
        this.packager = packager;
    }

    public void packageClasses(Configuration configuration, File tempDirectory) throws Exception {
        this.packager.packageClasses(configuration.getString(NAME.key()),
                tempDirectory.getPath(), configuration.getString(OUTPUT.key()));
        (new ClearableDirectory(tempDirectory)).clearContent();
        if (!tempDirectory.delete()) {
            throw new IllegalStateException("Could not delete temporary directory: " + tempDirectory);
        }
    }

}
