package gradle.junit.selenium.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads configuration values from config.properties.
 * The static block loads the file once when the class is first used.
 */
public class ConfigReader {

    private static final Properties props = new Properties();

    static {
        try (InputStream input = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties not found in src/test/resources/");
            }
            props.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    // Private constructor — this is a utility class, never instantiate it
    private ConfigReader() {}

    /** Returns the value for a key. Throws if key is missing. */
    public static String get(String key) {
        String value = props.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Missing config property: " + key);
        }
        return value.trim();
    }

    /** Returns the value for a key, or a default value if the key is missing. */
    public static String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue).trim();
    }

    /** Returns the value as a boolean (true/false). */
    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key, "false"));
    }
}
