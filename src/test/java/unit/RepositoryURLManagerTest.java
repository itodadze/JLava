package unit;

import dependency.RepositoryURLManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static dependency.RepositoryCatalog.MAVEN_CENTRAL;

public class RepositoryURLManagerTest {

    @Test
    public void testMixedRepositoriesCustom() {
        Optional<String> result = getFromMixedRepository("J_CENTER",
                "custom", "custom");
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("custom", result.get());
    }

    @Test
    public void testMixedRepositoriesCatalog() {
        String repositoryInCatalogUrl = MAVEN_CENTRAL.url();

        Optional<String> result = getFromMixedRepository("MAVEN_CENTRAL",
                "other", MAVEN_CENTRAL.url());
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(repositoryInCatalogUrl, result.get());
    }

    private Optional<String> getFromMixedRepository(String inCatalog, String custom,
                                                    String search) {

        RepositoryURLManager manager = new RepositoryURLManager(List.of(
                inCatalog, custom
        ));

        return manager.firstSatisfying(repository -> {
            if (repository.equals(search)) {
                return Stream.of(repository);
            } else {
                return Stream.empty();
            }
        });
    }

}
