package config;

public enum ConfigurationKey {
    NAME("name"),
    SOURCE("sourceDirs"),
    OUTPUT("outputDir"),
    REPOSITORIES("repositories"),
    DEPENDENCIES("dependencies");

    private final String key;
    public String key() { return this.key; }
    ConfigurationKey(String key) { this.key = key; }
}
