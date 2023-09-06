package dependency;

import logger.Logger;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static logger.LogMessages.*;

public class DependencyDownloader {
    private final Logger logger;
    private final DependencyResponseProcessor responseProcessor;
    private final CloseableHttpClient httpClient;

    /**
     * Constructs the instance of the DependencyDownloader class.
     *
     * @param logger            for logging messages and errors.
     * @param responseProcessor for processing the response after the dependency is
     *                          found.
     * @param httpClient        for http communication.
     */
    public DependencyDownloader(Logger logger,
                                DependencyResponseProcessor responseProcessor,
                                CloseableHttpClient httpClient) {
        this.logger = logger;
        this.responseProcessor = responseProcessor;
        this.httpClient = httpClient;
    }

    /**
     * Downloads and returns the path of the dependency.
     *
     * @param repositoryUrlManager  user-provided repositories in which the dependency
     *                              is searched.
     * @param dependency            the dependency.
     * @return                      path to the downloaded dependency.
     * @throws Exception            if the dependency could not be found or the
     *                              processor had trouble processing it.
     */
    public String download(RepositoryURLManager repositoryUrlManager, String dependency) throws Exception {
        Optional<Map.Entry<String,CloseableHttpResponse>> response = repositoryUrlManager
                .firstSatisfying(repository -> tryRetrieveResponse(repository, dependency)
        );
        if (response.isPresent()) {
            this.logger.printLine(DEPENDENCY_DOWNLOAD_SUCCESS.string() + ": %s",
                    dependency);
            return this.responseProcessor.process(response.get().getValue(),
                    response.get().getKey(), dependency);
        } else {
            this.logger.printLine(DEPENDENCY_NOT_FOUND.string() + ": %s", dependency);
            throw new NoHttpResponseException(DEPENDENCY_NOT_FOUND.string());
        }
    }

    /**
     * Closes the http client.
     */
    public void close() {
        try {
            this.httpClient.close();
        } catch (Exception e) {
            this.logger.printLine(HTTP_ERROR.string() + ": %s", e.getMessage());
        }
    }

    private Stream<Map.Entry<String,CloseableHttpResponse>> tryRetrieveResponse(
            String repository, String dependency) {
        HttpGet httpget = new HttpGet(repository + "/" + dependency + ".jar");
        try {
            CloseableHttpResponse result = this.httpClient.execute(httpget);
            int statusCode = result.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                return Stream.of(Map.entry(repository, result));
            } else {
                return Stream.empty();
            }
        } catch (Exception e) {
            return Stream.empty();
        }
    }
}
