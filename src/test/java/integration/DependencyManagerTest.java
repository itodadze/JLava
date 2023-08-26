package integration;

import dependency.*;
import helper.MockHttpClientProvider;
import helper.StringLogger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utility.ClearableDirectory;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

public class DependencyManagerTest {

    private final String RES_PATH = Paths.get("src", "test", "resources", "manager").toString();

    @BeforeEach
    public void cleanDownloads() {
        (new ClearableDirectory(new File(Paths.get(RES_PATH, "download", "repo1")
                .toString()))).clearContent();
    }

    @Test
    public void testDependencyFetchMixed() {
        String content = "content";
        String cacheDirectory = Paths.get(RES_PATH, "cache").toString();
        String downloadDirectory = Paths.get(RES_PATH, "download").toString();
        String repo1 = "repo1";
        String repo2 = "repo2";
        String cached1Name = "cachedDependency1";
        String cached2Name = "cachedDependency2";
        String cached1 = Paths.get(cacheDirectory, repo1, cached1Name + ".jar").toString();
        String cached2 = Paths.get(cacheDirectory, repo2, cached2Name + ".jar").toString();
        String uncachedName = "uncached";
        String uncached = Paths.get(downloadDirectory, repo1, uncachedName + ".jar").toString();

        StringLogger logger = new StringLogger();
        RepositoryURLManager repositories = new RepositoryURLManager(List.of(repo1, repo2));

        try {
            DependencyResponseProcessor processor = new DependencyResponseProcessor(
                    logger, downloadDirectory);

            DependencyDownloader downloader = new DependencyDownloader(
                    logger, processor, MockHttpClientProvider.get(200, content));

            DependencyCacheManager cache = new DependencyCacheManager(
                    cacheDirectory, 100);
            cache.register(cached1);
            cache.register(cached2);

            DependencyManager manager = new DependencyManager(
                    logger, downloader, cache
            );
            List<String> paths = manager.fetchPaths(repositories, List.of(cached1Name, cached2Name, uncachedName));
            Assertions.assertEquals(List.of(cached1, cached2, uncached), paths);
        } catch(Exception e) {
            Assertions.fail("Exception thrown when not expected: " + e.getMessage());
        }
    }

}
