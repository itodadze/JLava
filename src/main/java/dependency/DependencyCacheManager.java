package dependency;

import logger.Logger;

import java.io.File;
import java.util.Optional;
import java.util.stream.Stream;

public class DependencyCacheManager {
    private final static String CACHE_DIRECTORY_PATH = ".cache";
    private final Logger logger;
    private final String dependencyDirectory;
    private final RepositoryURLManager repositoryUrlManager;
    public DependencyCacheManager(Logger logger, String dependencyDirectory,
                                  RepositoryURLManager repositoryUrlManager) {
        this.logger = logger;
        this.dependencyDirectory = dependencyDirectory;
        this.repositoryUrlManager = repositoryUrlManager;
    }

    public Optional<String> cached(String dependency) {
        return this.repositoryUrlManager.firstSatisfying(
                url -> {
                    String path = CACHE_DIRECTORY_PATH + "/" + url + "/" + dependency + ".jar";
                    if ((new File(path)).exists()) {
                        return Stream.of(path);
                    } else {
                        return Stream.empty();
                    }
                }
        );
    }

}
