package unit;

import helper.StringLogger;
import main.compiler.ClassCompiler;
import main.compiler.FileGatherer;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ClassCompilerTest {

    private final String testDirectoryPath = "src/test/unit/resources/class_compiler";
    private final String innerOutputPath = "unit/resources/class_compiler";

    @Before
    public void clearOutputDirectories() {
        clearDirectoryContent(new File(testDirectoryPath + "/empty/output"));
        clearDirectoryContent(new File(testDirectoryPath + "/unbranched/output"));
        clearDirectoryContent(new File(testDirectoryPath + "/branched/output"));
    }

    @Test
    public void testCompilerEmptyDirectory() {
        File[] outputInnerFiles = getCompiledInnerOutputFiles("empty", List.of());
        assertNotNull(outputInnerFiles);
        assertEquals(0, outputInnerFiles.length);
    }

    @Test
    public void testCompilerUnbranchedDirectory() {
        String srcDirectory = testDirectoryPath + "/unbranched/src";
        String outputDirectory = testDirectoryPath + "/unbranched/output";
        String outputInnerDirectory = outputDirectory + "/" + innerOutputPath + "/unbranched/src";
        List<String> expectedJavaFiles = List.of(srcDirectory + "/Class.java");

        File[] outputInnerFiles = getCompiledInnerOutputFiles("unbranched", expectedJavaFiles);
        assertNotNull(outputInnerFiles);
        assertEquals(1, outputInnerFiles.length);

        assertTrue((new File(outputInnerDirectory + "/Class.class")).exists());
    }

    @Test
    public void testCompilerBranchedDirectory() {
        String srcDirectory = testDirectoryPath + "/branched/src";
        String outputDirectory = testDirectoryPath + "/branched/output";
        String outputInnerDirectory = outputDirectory + "/" + innerOutputPath + "/branched/src";
        List<String> expectedJavaFiles = List.of(
                srcDirectory + "/dir1/Class1.java", srcDirectory + "/dir2/Class2.java"
        );

        File[] outputInnerFiles = getCompiledInnerOutputFiles("branched", expectedJavaFiles);
        assertNotNull(outputInnerFiles);
        assertEquals(1, outputInnerFiles.length);

        assertTrue((new File(outputInnerDirectory + "/dir1/Class1.class")).exists());
        assertTrue((new File(outputInnerDirectory + "/dir2/Class2.class")).exists());
    }

    private File[] getCompiledInnerOutputFiles(String directory, List<String> javaFiles) {
        String srcDirectory = testDirectoryPath + "/" + directory + "/src";
        String outputDirectory = testDirectoryPath + "/" + directory + "/output";
        StringLogger logger = new StringLogger();
        FileGatherer fileGatherer = mock(FileGatherer.class);

        when(fileGatherer.javaFilesFromSources(anyList())).thenReturn(javaFiles);
        ClassCompiler compiler = new ClassCompiler(logger, fileGatherer);
        compiler.compileClasses(List.of(srcDirectory), outputDirectory);
        return new File(outputDirectory).listFiles();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void clearDirectoryContent(File directory) {
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        clearDirectoryContent(file);
                    }
                    file.delete();
                }
            }
        }
    }

}
