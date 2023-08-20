package dependency;

import logger.Logger;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static logger.LogMessages.*;

public class DependencyDownloader {
    private final Logger logger;
    private final RepositoryURLManager repositoryUrlManager;
    private final CloseableHttpClient httpClient;

    public DependencyDownloader(Logger logger, RepositoryURLManager repositoryURLManager) {
        this.logger = logger;
        this.repositoryUrlManager = repositoryURLManager;
        // TODO: Authentication logic may be needed
        this.httpClient = HttpClients.createDefault();
    }

    // TODO: Cache downloaded dependencies
    public void download(String dependency) {
        List<String> repositories = this.repositoryUrlManager.getURLs();
        Optional<CloseableHttpResponse> response = repositories.stream().flatMap(
                repository -> {
                    HttpGet httpget = new HttpGet(repository + "/" + dependency);
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
        ).findFirst();
        if (response.isPresent()) {
            this.logger.printLine(DEPENDENCY_DOWNLOAD_SUCCESS.string() + ": %s", dependency);
        } else {
            this.logger.printLine(DEPENDENCY_NOT_FOUND.string() + ": %s", dependency);
        }
    }

    public void close() {
        try {
            this.httpClient.close();
        } catch(Exception e) {
            this.logger.printLine(HTTP_ERROR.string() + ": %s", e.getMessage());
        }
    }
}
