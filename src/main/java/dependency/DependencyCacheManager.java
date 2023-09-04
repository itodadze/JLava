package dependency;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import runner.JLava;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static runner.JLava.CACHE_DIRECTORY;
import static utility.Namer.DIRECTORY;
import static utility.Namer.JAR;

public enum DependencyCacheManager {
    INSTANCE;

    private final static int CACHE_AUTO_EVICTION_DAYS = 5;
    private final String cacheDirectory = CACHE_DIRECTORY;
    private final Cache<String, File> cache = createCache();

    /**
     * Registers the dependency represented by the path in the cache.
     *
     * @param path  path to the dependency.
     */
    public void register(String path) {
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
    public Optional<String> cached(RepositoryURLManager repositoryURLManager, String dependency) {
        return repositoryURLManager.firstSatisfying(
                url -> cachedWithRepository(url, dependency));
    }

    private Cache<String, File> createCache() {
        return Caffeine.newBuilder()
                .maximumWeight(JLava.DEFAULT_CACHE_SIZE_MB)
                .expireAfterAccess(CACHE_AUTO_EVICTION_DAYS, TimeUnit.DAYS)
                .removalListener(new DependencyRemovalListener())
                .weigher(new DependencyWeigher())
                .build();
    }

    private Stream<String> cachedWithRepository(String repositoryUrl, String dependency) {
        String path = Paths.get(this.cacheDirectory, DIRECTORY.name(repositoryUrl),
                JAR.name(dependency)).toString();
        if (cache.getIfPresent(path) != null) {
            return Stream.of(path);
        } else {
            return Stream.empty();
        }
    }
}
