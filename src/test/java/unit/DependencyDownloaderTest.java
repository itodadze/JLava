package unit;

import dependency.DependencyDownloader;
import dependency.DependencyResponseProcessor;
import dependency.RepositoryURLManager;
import helper.MockHttpClientProvider;
import helper.StringLogger;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static logger.LogMessages.DEPENDENCY_DOWNLOAD_SUCCESS;
import static logger.LogMessages.DEPENDENCY_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DependencyDownloaderTest {

    @Test
    public void testDependencyDownloadExistingUrl() {
        String dependencyName = "dependency";
        String outputDirectory = "test";
        String repository = "repo";
        StringLogger logger = new StringLogger();
        try {
            String path = getDownloadedDependencyPath(dependencyName, outputDirectory,
                    repository, logger, 200);
            assertEquals(Paths.get(outputDirectory, repository,
                    dependencyName + ".jar").toString(), path);
            assertTrue(logger.getLog().startsWith(
                    DEPENDENCY_DOWNLOAD_SUCCESS.string() + ": " + dependencyName));
        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test
    public void testDependencyDownloadNonexistentUrl() {
        String dependencyName = "dependency";
        String outputDirectory = "test";
        String repository = "repo";
        StringLogger logger = new StringLogger();
        try {
            getDownloadedDependencyPath(dependencyName, outputDirectory, repository, logger, 400);
            fail("Exception not thrown when expected");
        } catch (Exception e) {
            assertTrue(logger.getLog().startsWith(
                    DEPENDENCY_NOT_FOUND.string() + ": " + dependencyName));
        }
    }

    public String getDownloadedDependencyPath(String name, String outputDirectory,
                                              String repository, StringLogger logger,
                                              int statusCode)
        throws Exception {
        String fullPath = Paths.get(outputDirectory, repository, name + ".jar").toString();

        DependencyResponseProcessor processor = mock(
                DependencyResponseProcessor.class);
        when(processor.process(any(CloseableHttpResponse.class), anyString(), anyString()))
                .thenReturn(fullPath);

        CloseableHttpClient httpClient = MockHttpClientProvider.get(statusCode, "");
        RepositoryURLManager repositoryURLManager = new RepositoryURLManager(List.of(repository));

        DependencyDownloader downloader = new DependencyDownloader(logger,
                processor, httpClient);
        return downloader.download(repositoryURLManager, name);
    }

}
