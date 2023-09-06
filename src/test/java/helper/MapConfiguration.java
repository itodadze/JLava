package helper;

import config.Configuration;

import java.util.List;
import java.util.Map;

public class MapConfiguration implements Configuration {
    private final Map<String, String> stringMap;
    private final Map<String, List<String>> listMap;

    public MapConfiguration(Map<String, String> stringMap, Map<String, List<String>> listMap) {
        this.stringMap = stringMap;
        this.listMap = listMap;
    }

    @Override
    public boolean hasKey(String key) {
        return this.stringMap.containsKey(key) || this.listMap.containsKey(key);
    }

    @Override
    public String getString(String key) throws Exception {
        if (this.stringMap.containsKey(key)) {
            return this.stringMap.get(key);
        } else {
            throw new Exception("Could not be found or converted");        }
    }
    @Override
    public List<String> getList(String key) throws Exception {
        if (this.listMap.containsKey(key)) {
            return this.listMap.get(key);
        } else {
            throw new Exception("Could not be found or converted");
        }
    }
    @Override
    public List<String> getListOrDefault(String key, List<String> defaultList) throws Exception {
        if (this.listMap.containsKey(key)) {
            return this.listMap.get(key);
        } else if (!this.stringMap.containsKey(key)) {
            return defaultList;
        } else {
            throw new Exception("Could not be converted");
        }
    }
}
