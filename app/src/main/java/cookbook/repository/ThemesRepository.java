package cookbook.repository;

import cookbook.model.ThemePreference;
import javafx.scene.Scene;

public class ThemesRepository {

    private static String theme = "dark_theme.css";

    public static void setTheme(String theme){
        ThemesRepository.theme = theme;
    }

    public static void applyTheme(Scene scene){
        scene.getStylesheets().clear();
        System.out.println(ThemePreference.loadTheme());
        scene.getStylesheets().add(ThemesRepository.class.getResource(ThemePreference.loadTheme()).toExternalForm());
    }
}
