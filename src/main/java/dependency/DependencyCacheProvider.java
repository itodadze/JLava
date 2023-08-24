package dependency;

import logger.Logger;
import utility.FileGatherer;

import java.util.List;

public class DependencyCacheProvider {
    public static final String CACHE_DIRECTORY = ".cache";
    private static DependencyCacheManager INSTANCE = null;
    private static int previousMaxCacheSizeMb = 0;

    public static synchronized DependencyCacheManager getInstance(Logger logger, int maxCacheSizeMb) {
        if (INSTANCE == null) {
            INSTANCE = new DependencyCacheManager(CACHE_DIRECTORY, maxCacheSizeMb);
            FileGatherer jarFileGatherer = new FileGatherer(logger, "jar");
            jarFileGatherer.filesFromSources(List.of(CACHE_DIRECTORY))
                    .forEach(path -> INSTANCE.register(path));

        } else if (previousMaxCacheSizeMb != maxCacheSizeMb) {
            INSTANCE.updateCacheSize(maxCacheSizeMb);
        }
        previousMaxCacheSizeMb = maxCacheSizeMb;
        return INSTANCE;
    }

}
