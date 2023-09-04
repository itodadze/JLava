package unit;

import helper.StringLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import packager.JarPackager;
import utility.Directory;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.jar.JarFile;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class JarPackagerTest {

    private static final String RES_PATH = Paths
            .get("src", "test", "resources", "packager").toString();

    @BeforeEach
    public void setup() {
        clearJarFiles();
        createClassFiles();
    }

    private void clearJarFiles() {
        try {
            (new Directory(Paths.get(RES_PATH, "basic", "jar").toFile())).clearContent();
            (new Directory(Paths.get(RES_PATH, "branched", "jar").toFile())).clearContent();
        } catch (Exception e) {
            fail("Exception thrown when not expected");
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createClassFiles() {
        try {
            (new File(Paths.get(RES_PATH, "basic", "temp", "Class.class")
                    .toString())).createNewFile();
            (new File(Paths.get(RES_PATH, "basic", "temp", "Class1.class")
                    .toString())).createNewFile();
            (new File(Paths.get(RES_PATH, "branched", "temp", "Class.class")
                    .toString())).createNewFile();
            (new File(Paths.get(RES_PATH, "branched", "temp", "branch", "Class.class")
                    .toString())).createNewFile();
        } catch(Exception e) {
            fail("Exception thrown when not expected: " + e.getMessage());
        }
    }

    @Test
    public void testJarPackagerBasic() {
        try(JarFile jar = new JarFile(makeJarFile("basic", "basic"))) {
            List<String> jarInnerClassesPaths = List.of("Class.class", "Class1.class");
            jarInnerClassesPaths.forEach(path -> assertNotNull(jar.getJarEntry(path)));
        } catch(Exception e) {
            fail("Exception thrown when not expected: " + e.getMessage());
        }
    }

    @Test
    public void testJarPackagerBranch() {
        try (JarFile jar = new JarFile(makeJarFile("branch", "branched"))) {
            List<String> jarInnerClassesPaths = List.of("Class.class", Paths.get("branch",
                    "Class.class").toString());
            jarInnerClassesPaths.forEach(path -> assertNotNull(jar.getJarEntry(path)));
        } catch(Exception e) {
            fail("Exception thrown when not expected: " + e.getMessage());
        }
    }

    private File makeJarFile(String name, String innerPath) throws Exception {
        String tempDirectory = Paths.get(RES_PATH, innerPath, "temp").toString();
        String outputDirectory = Paths.get(RES_PATH, innerPath, "jar").toString();
        String jarFilePath = Paths.get(outputDirectory, name + ".jar").toString();
        StringLogger logger = new StringLogger();
        JarPackager packager = new JarPackager(logger);
        packager.packageClasses(name, tempDirectory, outputDirectory);
        return new File(jarFilePath);
    }

}
