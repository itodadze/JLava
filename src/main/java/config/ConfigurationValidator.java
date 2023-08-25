package config;

import java.util.List;

public class ConfigurationValidator {
    public static class Factory {
        public static ConfigurationValidator createDefault() {
            return new ConfigurationValidator(
                    List.of(Configuration.NAME,
                            Configuration.SOURCE,
                            Configuration.OUTPUT,
                            Configuration.DEPENDENCIES)
            );
        }
    }

    private final List<String> requirements;
    private ConfigurationValidator(List<String> requirements) {
        this.requirements = requirements;
    }
    public boolean validate(Configuration configuration) {
        return this.requirements.stream().allMatch(configuration::hasKey);
    }

}
