package unit;

import config.JsonConfiguration;
import config.ConfigurationValidator;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigurationValidatorTest {

    private static final String RES_PATH = Paths
            .get("src", "test", "resources", "configuration").toString();
    private static final String VALID_JSON_PATH = Paths.get(RES_PATH, "valid.json").toString();
    private static final String VALID_REQ_MET_JSON_PATH = Paths.get(RES_PATH, "required.json").toString();

    @Test
    public void testConfigurationValidatorValid() {
        ConfigurationValidator validator = ConfigurationValidator.Factory.createDefault();
        try {
            assertTrue(validator.validate(new JsonConfiguration(VALID_REQ_MET_JSON_PATH)));
        } catch(Exception e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testConfigurationValidatorInvalid() {
        ConfigurationValidator validator = ConfigurationValidator.Factory.createDefault();
        try {
            assertFalse(validator.validate(new JsonConfiguration(VALID_JSON_PATH)));
        } catch(Exception e) {
            fail("Exception thrown when not expected");
        }
    }

}
