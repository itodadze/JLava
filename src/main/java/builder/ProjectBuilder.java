package builder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import compiler.ClassCompiler;
import dependency.DependencyManager;
import dependency.RepositoryURLManager;
import logger.Logger;
import packager.Packager;
import utility.ClearableDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static dependency.RepositoryCatalog.MAVEN_CENTRAL;
import static logger.LogMessages.*;

public class ProjectBuilder {
    private static final String NAME = "name";
    private static final String SOURCE = "sourceDirs";
    private static final String OUTPUT = "outputDir";
    private static final String REPOSITORIES = "repositories";
    private static final String DEPENDENCIES = "dependencies";

    private final Logger logger;
    private final ClassCompiler classCompiler;
    private final DependencyManager dependencyManager;
    private final Packager packager;
    public ProjectBuilder(Logger logger, ClassCompiler classCompiler,
                          DependencyManager dependencyManager,
                          Packager packager) {
        this.logger = logger;
        this.classCompiler = classCompiler;
        this.dependencyManager = dependencyManager;
        this.packager = packager;
    }

    public void build(String configFilePath) {
        File jsonFile = new File(configFilePath);
        if (jsonFile.exists() && jsonFile.isFile() &&
                jsonFile.getName().endsWith(".json")) {
            ObjectMapper mapper = new ObjectMapper();

            try {
                Map<String, Object> map = mapper.readValue(jsonFile,
                        new TypeReference<>() {});
                buildAccordingTo(mapper, map);
            } catch(IOException e) {
                this.logger.printLine(JSON_FILE_ERROR.string() + ": %s", e.getMessage());
            }
        } else {
            this.logger.printLine(CONFIG_NOT_FOUND.string() + ": %s", configFilePath);
        }
    }

    private void buildAccordingTo(ObjectMapper mapper, Map<String, Object> map) {
        try {
            assertRequirements(map);
            List<String> dependencyPaths = analyzeDependencies(mapper, map);
            String tempClassDirectory = analyzeClasses(mapper, map, dependencyPaths);
            packageClasses(mapper, map, tempClassDirectory);
            (new ClearableDirectory(new File(tempClassDirectory))).clearContent();
        } catch(Exception e) {
            this.logger.printLine(BUILD_ERROR.string() + ": %s", e.getMessage());
        }
    }

    private void assertRequirements(Map<String, Object> map) {
        List.of(NAME, SOURCE, OUTPUT, DEPENDENCIES).forEach(requirement -> {
            if (!map.containsKey(requirement)) {
                this.logger.printLine(CONFIG_FILE_ERROR.string() + ": no " + requirement);
                throw new IllegalArgumentException(CONFIG_FILE_ERROR.string());
            }
        });
    }

    private List<String> analyzeDependencies(ObjectMapper mapper, Map<String, Object> map) {
        List<String> repositories;
        if (map.containsKey(REPOSITORIES)) {
            repositories = accessList(mapper, map, REPOSITORIES);
        } else {
            repositories = List.of(MAVEN_CENTRAL.url());
        }
        List<String> dependencies = accessList(mapper, map, DEPENDENCIES);
        return this.dependencyManager.fetchPaths(
                new RepositoryURLManager(repositories), dependencies
        );
    }

    private String analyzeClasses(ObjectMapper mapper, Map<String, Object> map, List<String> dependencies) {
        List<String> sources = accessList(mapper, map, SOURCE);
        String output = mapper.convertValue(map.get(OUTPUT), String.class);
        String tempClassDirectory = Paths.get(output, "temp").toString();
        if ((new File(tempClassDirectory)).mkdir()) {
            this.classCompiler.compileClasses(sources, dependencies, tempClassDirectory);
        }
        return tempClassDirectory;
    }

    private void packageClasses(ObjectMapper mapper, Map<String, Object> map,
                                String tempClassDirectory) throws Exception {
        this.packager.packageClasses(mapper.convertValue(map.get(NAME), String.class),
                tempClassDirectory, mapper.convertValue(map.get(OUTPUT), String.class));
    }

    private List<String> accessList(ObjectMapper mapper, Map<String, Object> map, String key)
            throws IllegalArgumentException{
        try {
            return mapper.convertValue(map.get(key),
                    new TypeReference<>() {});
        } catch (IllegalArgumentException e) {
            this.logger.printLine(JSON_FILE_ERROR.string() + ": %s", e.getMessage());
            throw e;
        }
    }

}
