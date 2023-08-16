package unit;

import helper.StringLogger;
import compiler.ClassCompiler;
import compiler.FileGatherer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClassCompilerTest {

    private static final String RES_PATH = "src/test/resources/class_compiler";
    private static final String INNER_OUTPUT_PATH = "class_compiler";

    @BeforeEach
    public void clearOutputDirectories() {
        clearDirectoryContent(new File(RES_PATH + "/empty/output"));
        clearDirectoryContent(new File(RES_PATH + "/unbranched/output"));
        clearDirectoryContent(new File(RES_PATH + "/branched/output"));
    }

    @Test
    public void testCompilerEmptyDirectory() {
        File[] outputInnerFiles = getCompiledInnerOutputFiles("empty", List.of());
        assertNotNull(outputInnerFiles);
        assertEquals(0, outputInnerFiles.length);
    }

    @Test
    public void testCompilerUnbranchedDirectory() {
        String srcDirectory = RES_PATH + "/unbranched/src";
        String outputDirectory = RES_PATH + "/unbranched/output";
        String outputInnerDirectory = outputDirectory + "/" + INNER_OUTPUT_PATH + "/unbranched/src";
        List<String> expectedJavaFiles = List.of(srcDirectory + "/Class.java");

        File[] outputInnerFiles = getCompiledInnerOutputFiles("unbranched", expectedJavaFiles);
        assertNotNull(outputInnerFiles);
        assertEquals(1, outputInnerFiles.length);

        assertTrue((new File(outputInnerDirectory + "/Class.class")).exists());
    }

    @Test
    public void testCompilerBranchedDirectory() {
        String srcDirectory = RES_PATH + "/branched/src";
        String outputDirectory = RES_PATH + "/branched/output";
        String outputInnerDirectory = outputDirectory + "/" + INNER_OUTPUT_PATH + "/branched/src";
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
        String srcDirectory = RES_PATH + "/" + directory + "/src";
        String outputDirectory = RES_PATH + "/" + directory + "/output";
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
