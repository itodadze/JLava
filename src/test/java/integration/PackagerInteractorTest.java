package integration;

import builder.PackagerInteractor;
import config.Configuration;
import helper.MapConfiguration;
import helper.StringLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import packager.JarPackager;
import utility.Directory;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

import static config.ConfigurationKey.NAME;
import static config.ConfigurationKey.OUTPUT;
import static org.junit.jupiter.api.Assertions.*;

public class PackagerInteractorTest {

    private final static String RES_PATH = Paths.get("src", "test", "resources",
            "interactor", "packager").toString();
    private final static Directory TEMP = new Directory(Paths.get(RES_PATH, "temp").toFile());
    private final static Directory DEST = new Directory(Paths.get(RES_PATH, "dest").toFile());

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @BeforeEach
    public void fillTempDirClearOutput() {
        try {
            TEMP.makeIfNotExists();
            TEMP.clearContent();
            Paths.get(RES_PATH, "temp", "Class.class").toFile().createNewFile();
            DEST.makeIfNotExists();
            DEST.clearContent();
        } catch (Exception e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testPackagerPackagesDeletesTemp() {
        String name = "name";
        Configuration configuration = new MapConfiguration(
                Map.of(NAME.key(), name, OUTPUT.key(), Paths.get(RES_PATH, "dest")
                        .toString()), Map.of()
        );
        PackagerInteractor interactor = new PackagerInteractor(
                new JarPackager(new StringLogger())
        );
        File temp = Paths.get(RES_PATH, "temp").toFile();
        try {
            interactor.packageClasses(configuration, temp);
            assertTrue(Paths.get(RES_PATH, "dest", name + ".jar").toFile().exists());
            assertFalse(temp.exists());
        } catch (Exception e) {
            fail("Exception thrown when not expected: " + e.getMessage());
        }
    }

}
