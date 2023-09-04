package builder;

import config.Configuration;
import dependency.DependencyManager;
import dependency.RepositoryURLManager;

import java.util.List;

import static config.ConfigurationKey.DEPENDENCIES;
import static config.ConfigurationKey.REPOSITORIES;
import static dependency.RepositoryCatalog.MAVEN_CENTRAL;

public class DependencyInteractor {
    private final DependencyManager dependencyManager;

    public DependencyInteractor(DependencyManager dependencyManager) {
        this.dependencyManager = dependencyManager;
    }

    public List<String> fetchPaths(Configuration configuration) throws Exception {
        List<String> repositories = configuration.getListOrDefault(
                REPOSITORIES.key(), List.of(MAVEN_CENTRAL.url()));
        List<String> dependencies = configuration.getList(DEPENDENCIES.key());
        return this.dependencyManager.fetchPaths(
                new RepositoryURLManager(repositories), dependencies
        );
    }

}
