package dependency;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class DependencyCacheManager {
    public final static String CACHE_DIRECTORY_PATH = ".cache";
    private final static int CACHE_AUTO_EVICTION_DAYS = 5;
    private Cache<String, File> cache;
    public DependencyCacheManager(int maxCacheSizeMb) {
        this.cache = createCache(maxCacheSizeMb);
    }

    public synchronized void save(String path) {
        this.cache.put(path, new File(path));
    }

    public synchronized Optional<String> cached(RepositoryURLManager repositoryURLManager, String dependency) {
        return repositoryURLManager.firstSatisfying(
                url -> {
                    String path = Paths.get(CACHE_DIRECTORY_PATH, url, dependency + ".jar").toString();
                    if (cache.getIfPresent(path) != null) {
                        return Stream.of(path);
                    } else {
                        return Stream.empty();
                    }
                }
        );
    }

    public synchronized void updateCacheSize(int maxCacheSizeMb) {
        Cache<String, File> newCache = createCache(maxCacheSizeMb);
        newCache.putAll(this.cache.asMap());
        this.cache = newCache;
    }

    private Cache<String, File> createCache(int maxCacheSizeMb) {
        return Caffeine.newBuilder()
                .maximumWeight(maxCacheSizeMb)
                .expireAfterAccess(CACHE_AUTO_EVICTION_DAYS, TimeUnit.DAYS)
                .removalListener(new DependencyRemovalListener())
                .weigher(new DependencyWeigher())
                .build();
    }

}
