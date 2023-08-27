package config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;
import java.util.Map;

import static logger.LogMessages.CONFIG_NOT_FOUND;

/**
 * A class responsible for providing easy access to the configuration file content.
 */
public class Configuration {

    /**
     * Key for the project's name.
     */
    public static final String NAME = "name";
    /**
     * Key for the source files' paths.
     */
    public static final String SOURCE = "sourceDirs";
    /**
     * Key for the output directory path.
     */
    public static final String OUTPUT = "outputDir";
    /**
     * Key for the dependency repositories.
     */
    public static final String REPOSITORIES = "repositories";
    /**
     * Key for the dependency paths.
     */
    public static final String DEPENDENCIES = "dependencies";
    private final ObjectMapper mapper;
    private final Map<String, Object> map;

    /**
     * Constructs a Configuration class instance
     *
     * @param configFilePath    config file path.
     * @throws Exception        if file can not be found or correctly converted.
     */
    public Configuration(String configFilePath) throws Exception {
        File file = new File(configFilePath);
        if (file.exists() && file.isFile() && file.getName().endsWith(".json")) {
            this.mapper = new ObjectMapper();
            this.map = this.mapper.readValue(file, new TypeReference<>(){});
        } else {
            throw new IllegalArgumentException(CONFIG_NOT_FOUND.string() + ": " + configFilePath);
        }
    }

    public boolean hasKey(String key) {
        return this.map.containsKey(key);
    }

    /**
     * Get the value as a string.
     *
     * @param key           the key value.
     * @return              the value as string.
     * @throws Exception    if the value can not be converted to string.
     */
    public String getString(String key) throws Exception {
        try {
            return this.mapper.convertValue(
                    this.map.get(key), String.class
            );
        } catch(IllegalArgumentException e) {
            throw new Exception(e);
        }
    }

    /**
     * Get the value as a list of strings.
     *
     * @param key           the key value.
     * @return              the value as a list of strings.
     * @throws Exception    if the value can not be converted to a list.
     */
    public List<String> getList(String key) throws Exception {
        try {
            return this.mapper.convertValue(
                    this.map.get(key), new TypeReference<>() {
                    }
            );
        } catch(IllegalArgumentException e) {
            throw new Exception(e);
        }
    }

}
