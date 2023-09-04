package builder;

import config.Configuration;
import config.ConfigurationValidator;
import config.JsonConfiguration;
import logger.Logger;

import java.io.File;
import java.util.List;

import static logger.LogMessages.*;

/**
 * A class responsible for building a user's Java project.
 */
public class ProjectBuilder {
    private final Logger logger;
    private final CompilerInteractor compilerInteractor;
    private final DependencyInteractor dependencyInteractor;
    private final PackagerInteractor packagerInteractor;


    public ProjectBuilder(Logger logger, CompilerInteractor compilerInteractor,
                          DependencyInteractor dependencyInteractor,
                          PackagerInteractor packagerInteractor) {
        this.logger = logger;
        this.compilerInteractor = compilerInteractor;
        this.dependencyInteractor = dependencyInteractor;
        this.packagerInteractor = packagerInteractor;
    }

    /**
     * Builds the project based on a provided configuration file path.
     *
     * @param configFilePath    path to the config file.
     */
    public void build(String configFilePath) {
        try {
            Configuration configuration = new JsonConfiguration(configFilePath);
            boolean validated = ConfigurationValidator.Factory
                    .createDefault().validate(configuration);
            if (!validated) {
                throw new IllegalArgumentException(
                        JSON_FILE_ERROR.string() + ": validation failed"
                );
            }
            buildAccordingTo(configuration);
        } catch (Exception e) {
            this.logger.printLine(JSON_FILE_ERROR.string() + ": %s", e.getMessage());
        }
    }

    private void buildAccordingTo(Configuration configuration) {
        try {
            List<String> dependencies = this.dependencyInteractor.fetchPaths(configuration);
            File tempDirectory = this.compilerInteractor.compileClasses(configuration, dependencies);
            this.packagerInteractor.packageClasses(configuration, tempDirectory);
        } catch (Exception e) {
            this.logger.printLine(BUILD_ERROR.string() + ": %s", e.getMessage());
        }
    }

}
