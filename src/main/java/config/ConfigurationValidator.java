package config;

import java.util.List;

import static config.ConfigurationKey.*;

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
                    List.of(NAME.key(),
                            SOURCE.key(),
                            OUTPUT.key(),
                            DEPENDENCIES.key())
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
