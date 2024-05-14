package cookbook.Controller;

import cookbook.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifyUserSceneController {

    private User user;

    @FXML
    Button closeButton;

    @FXML
    TextField nameField;

    @FXML
    TextField usernameField;

    @FXML
    TextField passwordField;

    @FXML
    CheckBox isAdminCheckBox;

    public void setUser(User user) {
        this.user = user;
        nameField.setText(user.getName());
        usernameField.setText(user.getUserName());
        passwordField.setText(user.getPassword());
        isAdminCheckBox.setSelected(user.getIsAdmin() == 1);
    }

    @FXML
    public void cancelClicked(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
