package config;

import java.util.List;

public interface Configuration {
    boolean hasKey(String key);
    String getString(String key) throws Exception;
    List<String> getList(String key) throws Exception;
    List<String> getListOrDefault(String key, List<String> defaultList) throws Exception;
}
