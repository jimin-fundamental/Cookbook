package cookbook.Controller;

import cookbook.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ModifyUserSceneController {

    private User user;

    @FXML
    Button closeButton;

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    public void cancelClicked(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
