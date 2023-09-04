package dependency;

import logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static logger.LogMessages.DEPENDENCIES_FETCH_FAILURE;
import static logger.LogMessages.DEPENDENCIES_FETCH_SUCCESS;

/**
 * A class responsible for managing the dependencies.
 */
public class DependencyManager {
    private final static int NUM_THREADS = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * A task of analyzing one dependency.
     */
    private class DependencyFetchTask implements Runnable {
        private static final Object LIST_LOCK = new Object();
        private final RepositoryURLManager repositoryURLManager;
        private final String dependency;
        private final List<String> paths;
        private final CountDownLatch latch;

        /**
         * Creates the task.
         *
         * @param repositoryURLManager  repositories in which to search dependency.
         * @param dependency            the dependency.
         * @param paths                 the list in which to save the path of the
         *                              dependency.
         * @param latch                 used for locking the list.
         */
        public DependencyFetchTask(RepositoryURLManager repositoryURLManager,
                                   String dependency, List<String> paths,
                                   CountDownLatch latch) {
            this.repositoryURLManager = repositoryURLManager;
            this.dependency = dependency;
            this.paths = paths;
            this.latch = latch;
        }
        @Override
        public void run() {
            Optional<String> cached = dependencyCacheManager
                    .cached(this.repositoryURLManager, this.dependency);
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
            addToList(path);
        }

        private void addToList(String path) {
            synchronized (LIST_LOCK) {
                this.paths.add(path);
            }
            this.latch.countDown();
        }
    }
    private final Logger logger;
    private final DependencyDownloader dependencyDownloader;
    private final DependencyCacheManager dependencyCacheManager;

    /**
     * Constructs a DependencyManager instance.
     *
     * @param logger                    for logging messages and errors.
     * @param dependencyDownloader      for downloading dependencies.
     * @param dependencyCacheManager    for managing the dependency cache.
     */
    public DependencyManager(Logger logger, DependencyDownloader dependencyDownloader,
                             DependencyCacheManager dependencyCacheManager) {
        this.logger = logger;
        this.dependencyDownloader = dependencyDownloader;
        this.dependencyCacheManager = dependencyCacheManager;
    }

    /**
     * Analyzes the provided dependencies and returns the paths to them from the cache.
     * If they are not in cache already, they are downloaded.
     *
     * @param repositoryURLManager  the repositories in which to search.
     * @param dependencies          the dependencies.
     * @return                      a list of paths to the dependencies.
     */
    public List<String> fetchPaths(RepositoryURLManager repositoryURLManager, List<String> dependencies) {
        CountDownLatch countDownLatch = new CountDownLatch(dependencies.size());
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
        List<String> paths = new ArrayList<>();
        try {
            dependencies.forEach(
                    dependency -> executorService.execute(new DependencyFetchTask(
                            repositoryURLManager, dependency, paths, countDownLatch))
            );
            countDownLatch.await();
            this.logger.printLine(DEPENDENCIES_FETCH_SUCCESS.string());
            return paths;
        } catch (Exception e) {
            this.logger.printLine(DEPENDENCIES_FETCH_FAILURE.string() + ": %s", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
            this.dependencyDownloader.close();
        }
    }

}
