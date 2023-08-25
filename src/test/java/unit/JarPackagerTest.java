package unit;

import helper.StringLogger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import packager.JarPackager;
import utility.ClearableDirectory;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.jar.JarFile;

public class JarPackagerTest {

    private static final String RES_PATH = Paths
            .get("src", "test", "resources", "packager").toString();

    @BeforeEach
    public void setup() {
        clearJarFiles();
        createClassFiles();
    }

    private void clearJarFiles() {
        (new ClearableDirectory(new File(Paths.get(RES_PATH, "basic", "jar")
                .toString()))).clearContent();
        (new ClearableDirectory(new File(Paths.get(RES_PATH, "branched", "jar")
                .toString()))).clearContent();
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
            Assertions.fail("Exception thrown when not expected: " + e.getMessage());
        }
    }

    @Test
    public void testJarPackagerBasic() {
        try(JarFile jar = new JarFile(makeJarFile("basic", "basic"))) {
            List<String> jarInnerClassesPaths = List.of("Class.class", "Class1.class");
            jarInnerClassesPaths.forEach(path -> Assertions.assertNotNull(jar.getJarEntry(path)));
        } catch(Exception e) {
            Assertions.fail("Exception thrown when not expected: " + e.getMessage());
        }
    }

    @Test
    public void testJarPackagerBranch() {
        try (JarFile jar = new JarFile(makeJarFile("branch", "branched"))) {
            List<String> jarInnerClassesPaths = List.of("Class.class", Paths.get("branch",
                    "Class.class").toString());
            jarInnerClassesPaths.forEach(path -> Assertions.assertNotNull(jar.getJarEntry(path)));
        } catch(Exception e) {
            Assertions.fail("Exception thrown when not expected: " + e.getMessage());
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
