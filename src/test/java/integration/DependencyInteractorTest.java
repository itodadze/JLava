package integration;

import builder.DependencyInteractor;
import config.Configuration;
import dependency.DependencyManager;
import dependency.RepositoryURLManager;
import helper.MapConfiguration;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static config.ConfigurationKey.DEPENDENCIES;
import static config.ConfigurationKey.REPOSITORIES;
import static dependency.RepositoryCatalog.J_CENTER;
import static dependency.RepositoryCatalog.MAVEN_CENTRAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DependencyInteractorTest {

    @Test
    public void testDependencyInteractorEmptyRepositories() {
        String dependencyName = "dependency";
        String expected = MAVEN_CENTRAL.url() + "/" + dependencyName;
        Configuration configuration = new MapConfiguration(
                Map.of(), Map.of(DEPENDENCIES.key(), List.of(dependencyName))
        );
        DependencyManager manager = dependencyManagerWithFirstMatchingRepositoryUrl();
        DependencyInteractor interactor = new DependencyInteractor(manager);
        try {
            List<String> result = interactor.fetchPaths(configuration);
            assertEquals(result, List.of(expected));
        } catch (Exception e) {
            fail("Exception thrown when not expected: " + e.getMessage());
        }
    }

    @Test
    public void testDependencyInteractorFilledRepositories() {
        String dependencyName = "dependency";
        String expected = J_CENTER.url() + "/" + dependencyName;
        Configuration configuration = new MapConfiguration(Map.of(),
                Map.of(REPOSITORIES.key(), List.of("J_CENTER"),
                        DEPENDENCIES.key(), List.of(dependencyName))
        );
        DependencyManager manager = dependencyManagerWithFirstMatchingRepositoryUrl();
        DependencyInteractor interactor = new DependencyInteractor(manager);
        try {
            List<String> result = interactor.fetchPaths(configuration);
            assertEquals(result, List.of(expected));
        } catch (Exception e) {
            fail("Exception thrown when not expected: " + e.getMessage());
        }
    }

    private DependencyManager dependencyManagerWithFirstMatchingRepositoryUrl() {
        DependencyManager manager = mock(DependencyManager.class);
        when(manager.fetchPaths(any(RepositoryURLManager.class), anyList())).thenAnswer(
                invocation -> {
                    RepositoryURLManager urlManager = invocation.getArgument(0);
                    List<String> dependencies = invocation.getArgument(1);
                    return dependencies.stream().map(dep ->
                            urlManager.firstSatisfying(i -> Stream.of(i + "/" + dep)).orElse(null)
                    ).collect(Collectors.toList());
                }
        );
        return manager;
    }
}
