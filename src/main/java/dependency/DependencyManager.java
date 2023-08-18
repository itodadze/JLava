package dependency;

import logger.Logger;

public class DependencyManager {
    private final Logger logger;
    private final DependencyDownloader dependencyDownloader;

    public DependencyManager(Logger logger, DependencyDownloader dependencyDownloader) {
        this.logger = logger;
        this.dependencyDownloader = dependencyDownloader;
    }
}
