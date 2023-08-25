package config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;
import java.util.Map;

import static logger.LogMessages.CONFIG_NOT_FOUND;

public class Configuration {
    public static final String NAME = "name";
    public static final String SOURCE = "sourceDirs";
    public static final String OUTPUT = "outputDir";
    public static final String REPOSITORIES = "repositories";
    public static final String DEPENDENCIES = "dependencies";
    private final ObjectMapper mapper;
    private final Map<String, Object> map;

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

    public String getString(String key) throws Exception {
        try {
            return this.mapper.convertValue(
                    this.map.get(key), String.class
            );
        } catch(IllegalArgumentException e) {
            throw new Exception(e);
        }
    }

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
