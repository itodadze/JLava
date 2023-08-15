package unit;

import main.compiler.FileGatherer;
import main.util.LogMessages;
import org.junit.Test;

import helper.StringLogger;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileGathererTest {

    @Test
    public void testGatherFromInvalidSources() {
        StringLogger logger = new StringLogger();
        FileGatherer fileGatherer = new FileGatherer(logger);
        fileGatherer.javaFilesFromSources(List.of("src/test/unit/resources/file_gatherer/invalid"));
        assertTrue(logger.getLog().startsWith(LogMessages.FILE_PATH_NOT_FOUND));
    }

    @Test
    public void testGatherFromSingleJavaFile() {
        StringLogger logger = new StringLogger();
        FileGatherer fileGatherer = new FileGatherer(logger);
        String javaFilePath = "src/test/unit/resources/file_gatherer/SingleJavaClass.java";
        List<String> result = fileGatherer.javaFilesFromSources(
                List.of(javaFilePath));
        assertEquals(1, result.size());
        assertEquals(javaFilePath, result.get(0));
    }

    @Test
    public void testGatherFromUnbranched() {
        StringLogger logger = new StringLogger();
        FileGatherer fileGatherer = new FileGatherer(logger);
        String src = "src/test/unit/resources/file_gatherer/unbranched";
        List<String> result = fileGatherer.javaFilesFromSources(
                List.of(src));
        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(src +"/Class1.java", src + "/Class2.java")));
    }

    @Test
    public void testGatherFromBranched() {
        StringLogger logger = new StringLogger();
        FileGatherer fileGatherer = new FileGatherer(logger);
        String src = "src/test/unit/resources/file_gatherer/branched";
        List<String> result = fileGatherer.javaFilesFromSources(
                List.of(src));
        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(src + "/Class1.java", src + "/branch/Class2.java")));
    }

    @Test
    public void testGatherFromMultiple() {
        StringLogger logger = new StringLogger();
        FileGatherer fileGatherer = new FileGatherer(logger);
        String branchedSrc = "src/test/unit/resources/file_gatherer/branched";
        String unbranchedSrc = "src/test/unit/resources/file_gatherer/unbranched";
        List<String> result = fileGatherer.javaFilesFromSources(
                List.of(branchedSrc, unbranchedSrc));
        assertEquals(4, result.size());
        assertTrue(result.containsAll(List.of(
                branchedSrc + "/Class1.java", branchedSrc + "/branch/Class2.java",
                unbranchedSrc + "/Class1.java", unbranchedSrc + "/Class2.java"
        )));
    }

}
