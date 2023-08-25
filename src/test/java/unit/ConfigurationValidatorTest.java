package unit;

import config.Configuration;
import config.ConfigurationValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class ConfigurationValidatorTest {

    private static final String RES_PATH = Paths
            .get("src", "test", "resources", "configuration").toString();
    private static final String VALID_JSON_PATH = Paths.get(RES_PATH, "valid.json").toString();
    private static final String VALID_REQ_MET_JSON_PATH = Paths.get(RES_PATH, "required.json").toString();

    @Test
    public void testConfigurationValidatorValid() {
        ConfigurationValidator validator = ConfigurationValidator.Factory.createDefault();
        try {
            Assertions.assertTrue(validator.validate(new Configuration(VALID_REQ_MET_JSON_PATH)));
        } catch(Exception e) {
            Assertions.fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testConfigurationValidatorInvalid() {
        ConfigurationValidator validator = ConfigurationValidator.Factory.createDefault();
        try {
            Assertions.assertFalse(validator.validate(new Configuration(VALID_JSON_PATH)));
        } catch(Exception e) {
            Assertions.fail("Exception thrown when not expected");
        }
    }

}
