package unit;

import dependency.DependencyCacheManager;
import dependency.RepositoryURLManager;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static runner.JLava.CACHE_DIRECTORY;

public class DependencyCacheManagerTest {

    @Test
    public void testEnoughStorageCached() {
        String repository = "enough";
        String dependency = "example";
        DependencyCacheManager manager = DependencyCacheManager.INSTANCE;
        String path = Paths.get(CACHE_DIRECTORY, repository, dependency + ".jar").toString();
        manager.register(path);
        Optional<String> result = manager.cached(
                new RepositoryURLManager(List.of(repository)), dependency
        );
        assertTrue(result.isPresent());
        assertEquals(path, result.get());
    }

}
