package builder;

import compiler.ClassCompiler;
import config.Configuration;
import config.ConfigurationValidator;
import dependency.DependencyManager;
import dependency.RepositoryURLManager;
import logger.Logger;
import packager.Packager;
import utility.ClearableDirectory;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import static dependency.RepositoryCatalog.MAVEN_CENTRAL;
import static logger.LogMessages.*;

/**
 * A class responsible for building a user's Java project.
 */
public class ProjectBuilder {
    private final Logger logger;
    private final ClassCompiler classCompiler;
    private final DependencyManager dependencyManager;
    private final Packager packager;

    /**
     * Constructs a ProjectBuilder instance.
     *
     * @param logger            for logging messages and errors.
     * @param classCompiler     for compiling java classes.
     * @param dependencyManager for managing required dependencies.
     * @param packager          for packaging the compiled classes.
     */
    public ProjectBuilder(Logger logger, ClassCompiler classCompiler,
                          DependencyManager dependencyManager,
                          Packager packager) {
        this.logger = logger;
        this.classCompiler = classCompiler;
        this.dependencyManager = dependencyManager;
        this.packager = packager;
    }

    /**
     * Builds the project based on a provided configuration file path.
     *
     * @param configFilePath    path to the config file.
     */
    public void build(String configFilePath) {
        try {
            Configuration configuration = new Configuration(configFilePath);
            boolean validated = ConfigurationValidator.Factory
                    .createDefault().validate(configuration);
            if (!validated) throw new IllegalArgumentException(
                    JSON_FILE_ERROR.string() + ": validation failed"
            );
            buildAccordingTo(configuration);
        } catch(Exception e) {
            this.logger.printLine(JSON_FILE_ERROR.string() + ": %s", e.getMessage());
        }
    }

    private void buildAccordingTo(Configuration configuration) {
        try {
            List<String> dependencies = getDependencies(configuration);
            File tempDirectory = compileClasses(configuration, dependencies);
            packageClasses(configuration, tempDirectory);
        } catch(Exception e) {
            this.logger.printLine(BUILD_ERROR.string() + ": %s", e.getMessage());
        }
    }

    private List<String> getDependencies(Configuration configuration) throws Exception {
        List<String> repositories;
        if (configuration.hasKey(Configuration.REPOSITORIES)) {
            repositories = configuration.getList(Configuration.REPOSITORIES);
        } else {
            repositories = List.of(MAVEN_CENTRAL.url());
        }
        List<String> dependencies = configuration.getList(Configuration.DEPENDENCIES);
        return this.dependencyManager.fetchPaths(
                new RepositoryURLManager(repositories), dependencies
        );
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File compileClasses(Configuration configuration, List<String> dependencies)
            throws Exception {
        List<String> sources = configuration.getList(Configuration.SOURCE);
        String output = configuration.getString(Configuration.OUTPUT);
        String tempDirectoryPath = Paths.get(output, "temp").toString();
        File tempDirectory = new File(tempDirectoryPath);
        tempDirectory.mkdir();
        this.classCompiler.compileClasses(sources, dependencies, tempDirectoryPath);
        return tempDirectory;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void packageClasses(Configuration configuration, File tempDirectory)
            throws Exception {
        this.packager.packageClasses(configuration.getString(Configuration.NAME),
                tempDirectory.getPath(), configuration.getString(Configuration.OUTPUT));
        (new ClearableDirectory(tempDirectory)).clearContent();
        tempDirectory.delete();
    }

}
