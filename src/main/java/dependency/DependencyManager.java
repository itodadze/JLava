package dependency;

import logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static logger.LogMessages.DEPENDENCIES_FETCH_FAILURE;
import static logger.LogMessages.DEPENDENCIES_FETCH_SUCCESS;

public class DependencyManager {
    private final static int NUM_THREADS = Runtime.getRuntime().availableProcessors() * 2;
    private class DependencyFetchTask implements Runnable {
        private static final Object LIST_LOCK = new Object();
        private final RepositoryURLManager repositoryURLManager;
        private final String dependency;
        private final List<String> paths;
        public DependencyFetchTask(RepositoryURLManager repositoryURLManager,
                                   String dependency, List<String> paths) {
            this.repositoryURLManager = repositoryURLManager;
            this.dependency = dependency;
            this.paths = paths;
        }
        @Override
        public void run() {
            Optional<String> cached = dependencyCacheManager.cached(this.repositoryURLManager,
                    this.dependency);
            String path;
            if (cached.isPresent()) {
                path = cached.get();
            } else {
                try {
                    path = dependencyDownloader.download(this.repositoryURLManager,
                            dependency);
                    dependencyCacheManager.register(path);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            synchronized(LIST_LOCK) {
                this.paths.add(path);
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

    public List<String> fetchPaths(RepositoryURLManager repositoryURLManager, List<String> dependencies) {
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
        List<String> paths = new ArrayList<>();
        dependencies.forEach(
                dependency -> executorService.execute(new DependencyFetchTask(
                        repositoryURLManager, dependency, paths))
        );
        try {
            executorService.wait();
            this.logger.printLine(DEPENDENCIES_FETCH_SUCCESS.string());
            return paths;
        } catch (Exception e) {
            this.logger.printLine(DEPENDENCIES_FETCH_FAILURE.string() + ": %s", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            this.dependencyDownloader.close();
        }
    }

}
