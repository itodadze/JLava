package dependency;

import logger.Logger;

import java.util.List;

public class DependencyDownloader {
    private final Logger logger;
    private final RepositoryURLManager repositoryUrlManager;

    public DependencyDownloader(Logger logger, RepositoryURLManager repositoryURLManager) {
        this.logger = logger;
        this.repositoryUrlManager = repositoryURLManager;
    }

    // TODO: Cache downloaded dependencies
    public void downloadDependencies(List<String> dependencies) {
        // dependency download
    }
}
