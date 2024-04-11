package cookbook.Controller;

import javafx.scene.Node;

import java.io.IOException;

import cookbook.Model.SceneModifier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Create_new_profile_scene_controller {

    @FXML
    private Button create_profile_button;

    @FXML
    private TextField name_textbox;

    @FXML
    private TextField pw_textbox;

    @FXML
    private TextField uName_textbox;

    @FXML
    void back_button_pressed(ActionEvent event) throws IOException {
        SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook.view/login_scene.fxml")), (Stage)((Node)event.getSource()).getScene().getWindow());
    }

    @FXML
    void create_profile_button_pressed(ActionEvent event) {
        String name = name_textbox.getText();
        String userName = uName_textbox.getText();
        String password = pw_textbox.getText();

        // store data in the database
    }

}
