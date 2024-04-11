package cookbook;

import cookbook.controller.SceneModifier;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        try {
            // Connection conn = DriverManager.getConnection("jdbc:mysql://sql11.freemysqlhosting.net/sql11698285?&user=sql11698285&password=BlmMYE2vhj&useSSL=false ");
            SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook.view/LoginScene.fxml")), stage);
        
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
