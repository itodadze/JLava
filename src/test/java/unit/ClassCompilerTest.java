package unit;

import helper.ClearableDirectory;
import helper.StringLogger;
import compiler.ClassCompiler;
import utility.FileGatherer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.mockito.Mockito.*;

public class ClassCompilerTest {

    private static final String RES_PATH = "src/test/resources/compiler";
    private static final String INNER_OUTPUT_PATH = "compiler";

    @BeforeEach
    public void clearOutputDirectories() {
        (new ClearableDirectory(new File(RES_PATH + "/empty/output"))).clearContent();
        (new ClearableDirectory(new File(RES_PATH + "/unbranched/output"))).clearContent();
        (new ClearableDirectory(new File(RES_PATH + "/branched/output"))).clearContent();
    }

    @Test
    public void testCompilerEmptyDirectory() {
        File[] outputInnerFiles = getCompiledInnerOutputFiles("empty", List.of());
        Assertions.assertNotNull(outputInnerFiles);
        Assertions.assertEquals(0, outputInnerFiles.length);
    }

    @Test
    public void testCompilerUnbranchedDirectory() {
        String srcDirectory = RES_PATH + "/unbranched/src";
        String outputDirectory = RES_PATH + "/unbranched/output";
        String outputInnerDirectory = outputDirectory + "/" + INNER_OUTPUT_PATH + "/unbranched/src";
        List<String> expectedJavaFiles = List.of(srcDirectory + "/Class.java");

        File[] outputInnerFiles = getCompiledInnerOutputFiles("unbranched", expectedJavaFiles);
        Assertions.assertNotNull(outputInnerFiles);
        Assertions.assertEquals(1, outputInnerFiles.length);

        Assertions.assertTrue((new File(outputInnerDirectory + "/Class.class")).exists());
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
        Assertions.assertNotNull(outputInnerFiles);
        Assertions.assertEquals(1, outputInnerFiles.length);

        Assertions.assertTrue((new File(outputInnerDirectory + "/dir1/Class1.class")).exists());
        Assertions.assertTrue((new File(outputInnerDirectory + "/dir2/Class2.class")).exists());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File[] getCompiledInnerOutputFiles(String directory, List<String> javaFiles) {
        String srcDirectory = RES_PATH + "/" + directory + "/src";
        String outputDirectory = RES_PATH + "/" + directory + "/output";
        File outputDirFile = new File(outputDirectory);
        if (!outputDirFile.exists()) outputDirFile.mkdir();
        StringLogger logger = new StringLogger();
        FileGatherer fileGatherer = mock(FileGatherer.class);

        when(fileGatherer.filesFromSources(anyList())).thenReturn(javaFiles);
        ClassCompiler compiler = new ClassCompiler(logger, fileGatherer);
        compiler.compileClasses(List.of(srcDirectory), outputDirectory);
        return outputDirFile.listFiles();
    }

}
