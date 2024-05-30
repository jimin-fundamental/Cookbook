package cookbook;

import cookbook.Controller.LoginSceneController;
import cookbook.Controller.RecipeViewController;
import cookbook.model.ThemePreference;
import cookbook.repository.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private static DatabaseManager dbManager;
    private static UserRepository userRepo;

    @Override
    public void start(Stage stage) throws Exception {
        try {
            // Connection conn = DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net/sql11698285?&user=sql11698285&password=BlmMYE2vhj&useSSL=false ");
            ThemePreference.saveTheme("/css/light_theme.css");
            // Initialize the database manager and user repository
            dbManager = new DatabaseManager();
            userRepo = new UserDao(dbManager);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookbook.view/LoginScene.fxml"));
            Scene newScene = new Scene(fxmlLoader.load());
            stage.setScene(newScene);
            LoginSceneController controller = fxmlLoader.getController();
            controller.setStage(stage);
            stage.show();
            // Set up the initial scene
            //SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook.view/LoginScene.fxml")), stage);
        
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

    // Accessor methods for dbManager and userRepo if needed elsewhere
    public static DatabaseManager getDatabaseManager() {
        return dbManager;
    }

    public static UserRepository getUserRepository() {
        return userRepo;
    }
}
