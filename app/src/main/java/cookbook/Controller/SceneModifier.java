package cookbook.controller;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneModifier {

    public static void change_scene (Parent root, Stage stage){
        stage.setScene(new Scene(root));
        stage.show();        
    }
}
