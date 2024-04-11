package cookbook.Controller;

import java.io.IOException;
import cookbook.Model.SceneModifier;

import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Login_scene_controller {

    @FXML
    private TextField pw_textbox;

    @FXML
    private Pane scene1;

    @FXML
    private Button signIn_button;

    @FXML
    private TextField uName_textbox;


    @FXML
    void signIn_button_clicked(ActionEvent event) throws IOException{
        String userName = uName_textbox.getText();
        String password = pw_textbox.getText();

        // check database for name and password
        boolean credentialsAreCorrect = true;

        if (credentialsAreCorrect){
            SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook.view/start_scene.fxml")), (Stage)((Node)event.getSource()).getScene().getWindow());
        }

    }

    @FXML
    void create_profile_button_pressed(ActionEvent event) throws IOException {
        SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook.view/create_new_profile_scene.fxml")), (Stage)((Node)event.getSource()).getScene().getWindow());
    }

}
