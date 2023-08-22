package unit;

import dependency.RepositoryURLManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dependency.RepositoryCatalog.MAVEN_CENTRAL;

public class RepositoryURLManagerTest {

    @Test
    public void testMixedRepositories() {
        String repositoryInCatalog = "MAVEN_CENTRAL";
        String repositoryInCatalogUrl = MAVEN_CENTRAL.url();
        String customRepositoryUrl = "custom";
        RepositoryURLManager manager = new RepositoryURLManager(List.of(
                repositoryInCatalog, customRepositoryUrl
        ));
        Assertions.assertEquals(List.of(repositoryInCatalogUrl, customRepositoryUrl), manager.getURLs());
    }

}
