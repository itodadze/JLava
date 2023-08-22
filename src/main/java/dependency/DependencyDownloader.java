package dependency;

import logger.Logger;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static logger.LogMessages.*;

public class DependencyDownloader {
    private final Logger logger;
    private final RepositoryURLManager repositoryUrlManager;
    private final DependencyResponseProcessor responseProcessor;
    private final CloseableHttpClient httpClient;

    public DependencyDownloader(Logger logger,
                                RepositoryURLManager repositoryURLManager,
                                DependencyResponseProcessor responseProcessor,
                                CloseableHttpClient httpClient) {
        this.logger = logger;
        this.repositoryUrlManager = repositoryURLManager;
        this.responseProcessor = responseProcessor;
        this.httpClient = httpClient;
    }

    public String download(String dependency) throws Exception {
        List<String> repositories = this.repositoryUrlManager.getURLs();
        Optional<CloseableHttpResponse> response = repositories.stream().flatMap(
                repository -> tryRetrieveResponse(repository + "/" + dependency)
        ).findFirst();
        if (response.isPresent()) {
            this.logger.printLine(DEPENDENCY_DOWNLOAD_SUCCESS.string() + ": %s",
                    dependency);
            return this.responseProcessor.process(response.get(), dependency);
        } else {
            this.logger.printLine(DEPENDENCY_NOT_FOUND.string() + ": %s", dependency);
            throw new NoHttpResponseException(DEPENDENCY_NOT_FOUND.string());
        }
    }

    public void close() {
        try {
            this.httpClient.close();
        } catch(Exception e) {
            this.logger.printLine(HTTP_ERROR.string() + ": %s", e.getMessage());
        }
    }

    private Stream<CloseableHttpResponse> tryRetrieveResponse(String url) {
        HttpGet httpget = new HttpGet(url);
        try {
            CloseableHttpResponse result = this.httpClient.execute(httpget);
            int statusCode = result.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                return Stream.of(result);
            } else {
                return Stream.empty();
            }
        } catch (Exception e) {
            return Stream.empty();
        }
    }
}
