package unit;

import dependency.RepositoryURLManager;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static dependency.RepositoryCatalog.MAVEN_CENTRAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RepositoryURLManagerTest {

    @Test
    public void testMixedRepositoriesCustom() {
        Optional<String> result = getFromMixedRepository("J_CENTER",
                "custom", "custom");
        assertTrue(result.isPresent());
        assertEquals("custom", result.get());
    }

    @Test
    public void testMixedRepositoriesCatalog() {
        String repositoryInCatalogUrl = MAVEN_CENTRAL.url();

        Optional<String> result = getFromMixedRepository("MAVEN_CENTRAL",
                "other", MAVEN_CENTRAL.url());
        assertTrue(result.isPresent());
        assertEquals(repositoryInCatalogUrl, result.get());
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
