package unit;

import compiler.FileGatherer;
import helper.StringLogger;
import org.junit.jupiter.api.Test;

import java.util.List;

import static logger.LogMessages.FILE_PATH_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileGathererTest {
    private static final String RES_PATH = "src/test/resources/gatherer";

    @Test
    public void testGatherFromInvalidSources() {
        StringLogger logger = new StringLogger();
        FileGatherer fileGatherer = new FileGatherer(logger);
        fileGatherer.javaFilesFromSources(List.of(RES_PATH + "/invalid"));
        assertTrue(logger.getLog().startsWith(FILE_PATH_NOT_FOUND.string()));
    }

    @Test
    public void testGatherFromSingleJavaFile() {
        StringLogger logger = new StringLogger();
        FileGatherer fileGatherer = new FileGatherer(logger);
        String javaFilePath = RES_PATH + "/SingleJavaClass.java";
        List<String> result = fileGatherer.javaFilesFromSources(
                List.of(javaFilePath));
        assertEquals(1, result.size());
        assertEquals(javaFilePath, result.get(0));
    }

    @Test
    public void testGatherFromUnbranched() {
        StringLogger logger = new StringLogger();
        FileGatherer fileGatherer = new FileGatherer(logger);
        String src = RES_PATH + "/unbranched";
        List<String> result = fileGatherer.javaFilesFromSources(
                List.of(src));
        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(src +"/Class1.java", src + "/Class2.java")));
    }

    @Test
    public void testGatherFromBranched() {
        StringLogger logger = new StringLogger();
        FileGatherer fileGatherer = new FileGatherer(logger);
        String src = RES_PATH + "/branched";
        List<String> result = fileGatherer.javaFilesFromSources(
                List.of(src));
        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(src + "/Class1.java", src + "/branch/Class2.java")));
    }

    @Test
    public void testGatherFromMultiple() {
        StringLogger logger = new StringLogger();
        FileGatherer fileGatherer = new FileGatherer(logger);
        String branchedSrc = RES_PATH + "/branched";
        String unbranchedSrc = RES_PATH + "/unbranched";
        List<String> result = fileGatherer.javaFilesFromSources(
                List.of(branchedSrc, unbranchedSrc));
        assertEquals(4, result.size());
        assertTrue(result.containsAll(List.of(
                branchedSrc + "/Class1.java", branchedSrc + "/branch/Class2.java",
                unbranchedSrc + "/Class1.java", unbranchedSrc + "/Class2.java"
        )));
    }

}
