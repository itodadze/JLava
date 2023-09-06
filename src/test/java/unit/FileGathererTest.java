package unit;

import utility.FileGatherer;
import helper.StringLogger;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static logger.LogMessages.FILE_PATH_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileGathererTest {
    private static final String RES_PATH = Paths
            .get("src", "test", "resources", "gatherer").toString();

    @Test
    public void testGatherFromInvalidSources() {
        StringLogger logger = new StringLogger();
        FileGatherer fileGatherer = new FileGatherer(logger, "java");
        fileGatherer.filesFromSources(List.of(Paths.get(RES_PATH, "invalid").toString()));
        assertTrue(logger.getLog().startsWith(FILE_PATH_NOT_FOUND.string()));
    }

    @Test
    public void testGatherFromSingleJavaFile() {
        StringLogger logger = new StringLogger();
        FileGatherer fileGatherer = new FileGatherer(logger, "java");
        String javaFilePath = Paths.get(RES_PATH, "SingleJavaClass.java").toString();
        List<String> result = fileGatherer.filesFromSources(
                List.of(javaFilePath));
        assertEquals(1, result.size());
        assertEquals(javaFilePath, result.get(0));
    }

    @Test
    public void testGatherFromUnbranched() {
        StringLogger logger = new StringLogger();
        FileGatherer fileGatherer = new FileGatherer(logger, "java");
        String src = Paths.get(RES_PATH, "unbranched").toString();
        List<String> result = fileGatherer.filesFromSources(
                List.of(src));
        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(Paths.get(src, "Class1.java")
                .toString(), Paths.get(src, "Class2.java").toString())));
    }

    @Test
    public void testGatherFromBranched() {
        StringLogger logger = new StringLogger();
        FileGatherer fileGatherer = new FileGatherer(logger, "java");
        String src = Paths.get(RES_PATH, "branched").toString();
        List<String> result = fileGatherer.filesFromSources(
                List.of(src));
        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(Paths.get(src, "Class1.java")
                .toString(), Paths.get(src, "branch", "Class2.java").toString())));
    }

    @Test
    public void testGatherFromMultiple() {
        StringLogger logger = new StringLogger();
        FileGatherer fileGatherer = new FileGatherer(logger, "java");
        String branchedSrc = Paths.get(RES_PATH, "branched").toString();
        String unbranchedSrc = Paths.get(RES_PATH, "unbranched").toString();
        List<String> result = fileGatherer.filesFromSources(
                List.of(branchedSrc, unbranchedSrc));
        assertEquals(4, result.size());
        assertTrue(result.containsAll(List.of(
                Paths.get(branchedSrc, "Class1.java").toString(), Paths
                        .get(branchedSrc, "branch", "Class2.java").toString(),
                Paths.get(unbranchedSrc, "Class1.java").toString(),
                Paths.get(unbranchedSrc, "Class2.java").toString()
        )));
    }

}
