package unit;

import dependency.DependencyDownloader;
import dependency.DependencyResponseProcessor;
import dependency.RepositoryURLManager;
import helper.StringLogger;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static logger.LogMessages.DEPENDENCY_DOWNLOAD_SUCCESS;
import static logger.LogMessages.DEPENDENCY_NOT_FOUND;
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
            String path = getDownloadedDependencyPath(dependencyName, outputDirectory, repository,
                    logger, 200);
            Assertions.assertEquals(outputDirectory + "/" + repository + "/" + dependencyName + ".jar",
                    path);
            Assertions.assertTrue(logger.getLog().startsWith(
                    DEPENDENCY_DOWNLOAD_SUCCESS.string() + ": " + dependencyName));
        } catch (Exception e) {
            Assertions.fail("Exception not expected: " + e.getMessage());
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
            Assertions.fail("Exception not thrown when expected");
        } catch (Exception e) {
            Assertions.assertTrue(logger.getLog().startsWith(
                    DEPENDENCY_NOT_FOUND.string() + ": " + dependencyName));
        }
    }

    public String getDownloadedDependencyPath(String name, String outputDirectory,
                                              String repository, StringLogger logger,
                                              int statusCode)
        throws Exception {
        String fullPath = outputDirectory + "/" + repository + "/" + name + ".jar";

        DependencyResponseProcessor processor = mock(
                DependencyResponseProcessor.class);
        when(processor.process(any(CloseableHttpResponse.class), anyString(), anyString()))
                .thenReturn(fullPath);

        CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
        CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(statusCode);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);

        RepositoryURLManager repositoryURLManager = new RepositoryURLManager(List.of(repository));

        DependencyDownloader downloader = new DependencyDownloader(logger, repositoryURLManager,
                processor, httpClient);
        return downloader.download(name);
    }

}
