package unit;

import dependency.DependencyCacheManager;
import dependency.RepositoryURLManager;
import helper.ClearableDirectory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class DependencyCacheManagerTest {

    private final static String RES_PATH = Paths.get("src", "test",
            "resources", "cache").toString();

    @Test
    public void testEnoughStorageCached() {
        int bigCacheMb = 10;
        String repository = "enough";
        String dependency = "example";
        DependencyCacheManager manager = new DependencyCacheManager(RES_PATH, bigCacheMb);
        String path = Paths.get(RES_PATH, repository, dependency + ".jar").toString();
        manager.register(path);
        Optional<String> result = manager.cached(
                new RepositoryURLManager(List.of(repository)), dependency
        );
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(path, result.get());
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void testNotEnoughStorageCached() {
        int smallCacheMb = 1;
        String repository = "more";
        String oldDependency = "shouldDelete";
        String newDependency = "shouldStay";
        DependencyCacheManager manager = new DependencyCacheManager(RES_PATH, smallCacheMb);
        (new ClearableDirectory(new File(Paths.get(RES_PATH, repository).toString()))).clearContent();
        String oldDependencyPath = Paths.get(RES_PATH, repository, oldDependency + ".jar").toString();
        String newDependencyPath = Paths.get(RES_PATH, repository, newDependency + ".jar").toString();
        try {
            File oldFile = new File(oldDependencyPath);
            oldFile.createNewFile();
            /* the file will be registered as 1 mb weight due to ceiling rounding */
            manager.register(oldDependencyPath);

            File newFile = new File(newDependencyPath);
            newFile.createNewFile();
            manager.register(newDependencyPath);

            Optional<String> resultNewFile = manager.cached(
                    new RepositoryURLManager(List.of(repository)), newDependency
            );
            Assertions.assertTrue(resultNewFile.isPresent());
            Assertions.assertEquals(newDependencyPath, resultNewFile.get());

            Optional<String> resultOldFile = manager.cached(
                    new RepositoryURLManager(List.of(repository)), oldDependency
            );
            Assertions.assertFalse(resultOldFile.isPresent());
        } catch (IOException e) {
            Assertions.fail("Exception occurred when not expected: " + e.getMessage());
        }
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void testReduceCacheSize() {
        int initialSize = 2;
        int reducedSize = 1;
        String repository = "reduce";
        String dependency1 = "dependency1";
        String dependency2 = "dependency2";
        DependencyCacheManager manager = new DependencyCacheManager(RES_PATH, initialSize);
        (new ClearableDirectory(new File(Paths.get(RES_PATH, repository).toString()))).clearContent();
        String dependencyPath1 = Paths.get(RES_PATH, repository, dependency1 + ".jar").toString();
        String dependencyPath2 = Paths.get(RES_PATH, repository, dependency2 + ".jar").toString();
        try {
            File oldFile = new File(dependencyPath1);
            oldFile.createNewFile();
            manager.register(dependencyPath1);

            File newFile = new File(dependencyPath2);
            newFile.createNewFile();
            manager.register(dependencyPath2);

            manager.updateCacheSize(reducedSize);

            int count = 0;
            Optional<String> resultDependency2 = manager.cached(
                    new RepositoryURLManager(List.of(repository)), dependency2
            );
            if (resultDependency2.isPresent()) count++;

            Optional<String> resultDependency1 = manager.cached(
                    new RepositoryURLManager(List.of(repository)), dependency1
            );
            if (resultDependency1.isPresent()) count++;
            Assertions.assertEquals(1, count);
        } catch (IOException e) {
            Assertions.fail("Exception occurred when not expected: " + e.getMessage());
        }
    }

}
