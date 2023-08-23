package dependency;

import logger.Logger;
import utility.FileGatherer;

import java.util.List;

import static dependency.DependencyCacheManager.CACHE_DIRECTORY_PATH;

public class DependencyCacheProvider {
    private static DependencyCacheManager INSTANCE = null;
    private static int previousMaxCacheSizeMb = 0;

    public synchronized DependencyCacheManager getInstance(Logger logger, int maxCacheSizeMb) {
        if (INSTANCE == null) {
            INSTANCE = new DependencyCacheManager(maxCacheSizeMb);
            FileGatherer jarFileGatherer = new FileGatherer(logger, "jar");
            jarFileGatherer.filesFromSources(List.of(CACHE_DIRECTORY_PATH))
                    .forEach(path -> INSTANCE.save(path));

        } else if (previousMaxCacheSizeMb != maxCacheSizeMb) {
            INSTANCE.updateCacheSize(maxCacheSizeMb);
        }
        previousMaxCacheSizeMb = maxCacheSizeMb;
        return INSTANCE;
    }

}
