package unit;

import config.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigurationTest {
    private static final String RES_PATH = Paths
            .get("src", "test", "resources", "configuration").toString();
    private static final String VALID_JSON_PATH = Paths.get(RES_PATH, "valid.json").toString();
    private static final String INVALID_JSON_PATH = Paths.get(RES_PATH, "invalid.json").toString();
    private static final String STRING_KEY = "string";
    private static final String LIST_KEY = "list";
    private static final String INVALID_KEY = "invalid";

    @Test
    public void testConfigurationInvalid() {
        try {
            new Configuration(INVALID_JSON_PATH);
            Assertions.fail("Exception not thrown on invalid json config file");
        } catch(Exception ignored) {
        }
    }

    @Test
    public void testConfigurationHasKey() {
        try {
            Configuration configuration = new Configuration(VALID_JSON_PATH);
            Assertions.assertTrue(configuration.hasKey(STRING_KEY));
            Assertions.assertTrue(configuration.hasKey(LIST_KEY));
            Assertions.assertFalse(configuration.hasKey(INVALID_KEY));
        } catch(Exception e) {
            Assertions.fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testConfigurationGetStringValid() {
        try {
            Configuration configuration = new Configuration(VALID_JSON_PATH);
            assertEquals(STRING_KEY, configuration.getString(STRING_KEY));
        } catch(Exception e) {
            Assertions.fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testConfigurationGetListValid() {
        try {
            Configuration configuration = new Configuration(VALID_JSON_PATH);
            List<String> expected = List.of("string1", "string2");
            assertEquals(expected, configuration.getList(LIST_KEY));
        } catch(Exception e) {
            Assertions.fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testConfigurationGetStringInvalid() {
        try {
            Configuration configuration = new Configuration(VALID_JSON_PATH);
            configuration.getString(LIST_KEY);
            Assertions.fail("Exception not thrown when expected");
        } catch(Exception ignored) {
        }
    }

    @Test
    public void testConfigurationGetListInvalid() {
        try {
            Configuration configuration = new Configuration(VALID_JSON_PATH);
            configuration.getList(STRING_KEY);
            Assertions.fail("Exception not thrown when expected");
        } catch(Exception ignored) {
        }
    }

}
