package dependency;

import logger.Logger;
import runner.JLava;
import utility.FileGatherer;

import java.util.List;

public class DependencyCacheProvider {
    private static DependencyCacheManager INSTANCE = null;
    private static int previousMaxCacheSizeMb = 0;

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
