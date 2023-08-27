package config;

import java.util.List;

/**
 * A class responsible for validating the configuration file.
 */
public class ConfigurationValidator {

    /**
     * Provides the validator.
     */
    public static class Factory {

        /**
         * Demands name and source, output, dependency paths to be present in
         * configuration.
         *
         * @return ConfigurationValidator instance.
         */
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

    /**
     * Validates that Configuration matches all requirements.
     *
     * @param configuration the configuration instance.
     * @return              true if and only if all the requirements are met.
     */
    public boolean validate(Configuration configuration) {
        return this.requirements.stream().allMatch(configuration::hasKey);
    }

}
