package unit;

import utility.Directory;
import helper.StringLogger;
import compiler.ClassCompiler;
import utility.FileGatherer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClassCompilerTest {
    private static final String RES_PATH = Paths
            .get("src", "test", "resources", "compiler").toString();
    private static final String INNER_OUTPUT_PATH = "compiler";

    @BeforeEach
    public void clearOutputDirectories() {
        try {
            (new Directory(Paths.get(RES_PATH, "empty", "output").toFile())).clearContent();
            (new Directory(Paths.get(RES_PATH, "unbranched", "output").toFile())).clearContent();
            (new Directory(Paths.get(RES_PATH, "branched", "output").toFile())).clearContent();
            (new Directory(Paths.get(RES_PATH, "dependency", "output").toFile())).clearContent();
        } catch (Exception e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testCompilerEmptyDirectory() {
        File[] outputInnerFiles = getCompiledInnerOutputFiles("empty", List.of(),
                List.of());
        assertNotNull(outputInnerFiles);
        assertEquals(0, outputInnerFiles.length);
    }

    @Test
    public void testCompilerUnbranchedDirectory() {
        String srcDirectory = Paths.get(RES_PATH, "unbranched", "src").toString();
        String outputDirectory = Paths.get(RES_PATH, "unbranched", "output").toString();
        String outputInnerDirectory = Paths.get(outputDirectory, INNER_OUTPUT_PATH,
                "unbranched", "src").toString();
        List<String> expectedJavaFiles = List.of(Paths.get(srcDirectory,
                "Class.java").toString());

        File[] outputInnerFiles = getCompiledInnerOutputFiles("unbranched", List.of(),
                expectedJavaFiles);
        assertNotNull(outputInnerFiles);
        assertEquals(1, outputInnerFiles.length);

        assertTrue((new File(Paths.get(outputInnerDirectory, "Class.class")
                .toString())).exists());
    }

    @Test
    public void testCompilerBranchedDirectory() {
        String srcDirectory = Paths.get(RES_PATH, "branched", "src").toString();
        String outputDirectory = Paths.get(RES_PATH, "branched", "output").toString();
        String outputInnerDirectory = Paths.get(outputDirectory, INNER_OUTPUT_PATH,
                "branched", "src").toString();
        List<String> expectedJavaFiles = List.of(
                Paths.get(srcDirectory, "dir1", "Class1.java").toString(),
                Paths.get(srcDirectory, "dir2", "Class2.java").toString()
        );

        File[] outputInnerFiles = getCompiledInnerOutputFiles("branched", List.of(),
                expectedJavaFiles);
        assertNotNull(outputInnerFiles);
        assertEquals(1, outputInnerFiles.length);

        assertTrue((new File(Paths.get(outputInnerDirectory, "dir1",
                "Class1.class").toString())).exists());
        assertTrue((new File(Paths.get(outputInnerDirectory, "dir2",
                "Class2.class").toString())).exists());
    }

    @Test
    public void testCompilerWithDependencies() {
        String srcDirectory = Paths.get(RES_PATH, "dependency", "src").toString();
        String dependencyDirectory = Paths.get(RES_PATH, "dependency", "lib").toString();
        List<String> dependencies = List.of(Paths.get(
                dependencyDirectory, "commons-io-2.4.jar").toString());
        List<String> expectedJavaFiles = List.of(
                Paths.get(srcDirectory, "Class.java").toString());

        File[] outputInnerFiles = getCompiledInnerOutputFiles("dependency", dependencies,
                expectedJavaFiles);

        assertNotNull(outputInnerFiles);
        assertEquals(1, outputInnerFiles.length);
    }

    @Test
    public void testCompilerWithoutDependencies() {
        String srcDirectory = Paths.get(RES_PATH, "lacking", "src").toString();
        List<String> expectedJavaFiles = List.of(
                Paths.get(srcDirectory, "Class.java").toString()
        );

        File[] outputInnerFiles = getCompiledInnerOutputFiles("lacking", List.of(),
                expectedJavaFiles);

        assertNotNull(outputInnerFiles);
        assertEquals(0, outputInnerFiles.length);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File[] getCompiledInnerOutputFiles(String directory, List<String> dependencies,
                                               List<String> javaFiles) {
        String srcDirectory = Paths.get(RES_PATH, directory, "src").toString();
        String outputDirectory = Paths.get(RES_PATH, directory, "output").toString();
        File outputDirFile = new File(outputDirectory);
        if (!outputDirFile.exists()) outputDirFile.mkdir();
        StringLogger logger = new StringLogger();
        FileGatherer fileGatherer = mock(FileGatherer.class);

        when(fileGatherer.filesFromSources(anyList())).thenReturn(javaFiles);
        ClassCompiler compiler = new ClassCompiler(logger, fileGatherer);
        compiler.compileClasses(List.of(srcDirectory), dependencies, outputDirectory);
        return outputDirFile.listFiles();
    }

}
