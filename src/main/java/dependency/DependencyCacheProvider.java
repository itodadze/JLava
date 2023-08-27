package dependency;

import logger.Logger;
import runner.JLava;
import utility.FileGatherer;

import java.util.List;

/**
 * A class responsible for providing the DependencyCacheManager.
 */
public class DependencyCacheProvider {
    private static DependencyCacheManager INSTANCE = null;
    private static int previousMaxCacheSizeMb = 0;

    /**
     * Returns the instance of the DependencyCacheManager.
     *
     * @param logger            for logging messages and errors.
     * @param maxCacheSizeMb    maximum cache size in megabytes.
     * @return                  DependencyCacheManager instance.
     */
    public static synchronized DependencyCacheManager getInstance(Logger logger, int maxCacheSizeMb) {
        if (INSTANCE == null) {
            INSTANCE = new DependencyCacheManager(JLava.CACHE_DIRECTORY, maxCacheSizeMb);
            FileGatherer jarFileGatherer = new FileGatherer(logger, "jar");
            jarFileGatherer.filesFromSources(List.of(JLava.CACHE_DIRECTORY))
                    .forEach(path -> INSTANCE.register(path));

        } else if (previousMaxCacheSizeMb != maxCacheSizeMb) {
            INSTANCE.updateCacheSize(maxCacheSizeMb);
        }
        previousMaxCacheSizeMb = maxCacheSizeMb;
        return INSTANCE;
    }

}
