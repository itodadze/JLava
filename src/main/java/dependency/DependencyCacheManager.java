package dependency;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * A class responsible for managing the jlava's dependency cache.
 */
public class DependencyCacheManager {
    private final static int CACHE_AUTO_EVICTION_DAYS = 5;
    private final String cacheDirectory;
    private Cache<String, File> cache;

    /**
     * Constructs the DependencyCacheManager instance.
     *
     * @param cacheDirectory    path to the directory which is going to be used as
     *                          cache directory.
     * @param maxCacheSizeMb    The maximum size of the cache in megabytes.
     */
    public DependencyCacheManager(String cacheDirectory, int maxCacheSizeMb) {
        this.cacheDirectory = cacheDirectory;
        this.cache = createCache(maxCacheSizeMb);
    }

    /**
     * Registers the dependency represented by the path in the cache.
     *
     * @param path  path to the dependency.
     */
    public synchronized void register(String path) {
        this.cache.put(path, new File(path));
    }

    /**
     * Returns the path to the dependency if it exists in the cache.
     *
     * @param repositoryURLManager  an instance of the repository url manager based
     *                              on user provided repositories.
     * @param dependency            the dependency.
     * @return                      the path to the cached dependency if it exists.
     */
    public synchronized Optional<String> cached(RepositoryURLManager repositoryURLManager, String dependency) {
        return repositoryURLManager.firstSatisfying(
                url -> {
                    String path = Paths.get(this.cacheDirectory,
                            url.replace('/', '-').replace(':', '-'),
                            dependency.replace('/', '.') + ".jar").toString();
                    if (cache.getIfPresent(path) != null) {
                        return Stream.of(path);
                    } else {
                        return Stream.empty();
                    }
                }
        );
    }

    /**
     * Updates the maximum cache size
     *
     * @param maxCacheSizeMb    new maximum cache size in megabytes.
     */
    public synchronized void updateCacheSize(int maxCacheSizeMb) {
        Cache<String, File> newCache = createCache(maxCacheSizeMb);
        newCache.putAll(this.cache.asMap());
        this.cache = newCache;
    }

    private synchronized Cache<String, File> createCache(int maxCacheSizeMb) {
        return Caffeine.newBuilder()
                .maximumWeight(maxCacheSizeMb)
                .expireAfterAccess(CACHE_AUTO_EVICTION_DAYS, TimeUnit.DAYS)
                .removalListener(new DependencyRemovalListener())
                .weigher(new DependencyWeigher())
                .build();
    }

}
