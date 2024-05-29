package cookbook.model;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ThemePreference {

    private static final String CONFIG_FILE = "config.properties";
    private static final String THEME_KEY = "theme";

    public static void saveTheme(String theme) {
        Properties properties = new Properties();
        properties.setProperty(THEME_KEY, theme);

        try (FileOutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadTheme() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            return properties.getProperty(THEME_KEY, "light_theme.css");
        } catch (IOException e) {
            e.printStackTrace();
            return "light_theme.css";
        }
    }
}

