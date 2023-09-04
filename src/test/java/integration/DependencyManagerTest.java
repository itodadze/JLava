package integration;

import dependency.*;
import helper.MockHttpClientProvider;
import helper.StringLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.Directory;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DependencyManagerTest {

    private final String RES_PATH = Paths.get("src", "test", "resources", "manager").toString();

    @BeforeEach
    public void cleanDownloads() {
        try {
            (new Directory(Paths.get(RES_PATH, "download", "repo1").toFile())).clearContent();
        } catch (Exception e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testDependencyFetchMixed() {
        String content = "content";
        String downloadDirectory = Paths.get(RES_PATH, "download").toString();
        String repo = "repo1";
        String uncachedName = "uncached";
        String uncached = Paths.get(downloadDirectory, repo, uncachedName + ".jar").toString();

        StringLogger logger = new StringLogger();
        RepositoryURLManager repositories = new RepositoryURLManager(List.of(repo));

        try {
            DependencyResponseProcessor processor = new DependencyResponseProcessor(
                    logger, downloadDirectory);

            DependencyDownloader downloader = new DependencyDownloader(
                    logger, processor, MockHttpClientProvider.get(200, content));

            DependencyCacheManager cache = DependencyCacheManager.INSTANCE;

            DependencyManager manager = new DependencyManager(
                    logger, downloader, cache
            );
            List<String> paths = manager.fetchPaths(repositories, List.of(uncachedName));
            assertEquals(Set.of(uncached), new HashSet<>(paths));
        } catch(Exception e) {
            fail("Exception thrown when not expected: " + e.getMessage());
        }
    }

}
