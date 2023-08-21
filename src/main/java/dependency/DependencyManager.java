package dependency;

import logger.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static logger.LogMessages.DEPENDENCIES_FETCH_FAILURE;
import static logger.LogMessages.DEPENDENCIES_FETCH_SUCCESS;

public class DependencyManager {
    private final static int NUM_THREADS = 6;
    private class DependencyFetchTask implements Runnable {
        private final String dependency;
        public DependencyFetchTask(String dependency) {
            this.dependency = dependency;
        }
        @Override
        public void run() {
            if (!dependencyCacheManager.isCached(this.dependency)) {
                dependencyDownloader.download(dependency);
            }
        }
    }
    private final Logger logger;
    private final DependencyDownloader dependencyDownloader;
    private final DependencyCacheManager dependencyCacheManager;

    public DependencyManager(Logger logger, DependencyDownloader dependencyDownloader,
                             DependencyCacheManager dependencyCacheManager) {
        this.logger = logger;
        this.dependencyDownloader = dependencyDownloader;
        this.dependencyCacheManager = dependencyCacheManager;
    }

    public void fetch(List<String> dependencies) {
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
        dependencies.forEach(
                dependency -> executorService.execute(new DependencyFetchTask(dependency))
        );
        try {
            executorService.wait();
            this.logger.printLine(DEPENDENCIES_FETCH_SUCCESS.string());
        } catch (Exception e) {
            this.logger.printLine(DEPENDENCIES_FETCH_FAILURE.string() + ": %s", e.getMessage());
        }
    }

}
