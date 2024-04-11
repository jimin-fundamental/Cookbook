package cookbook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import cookbook.Model.SceneModifier;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        try {
            
            SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook.view/login_scene.fxml")), stage);
        
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
