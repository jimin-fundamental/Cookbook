package cookbook.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import cookbook.DatabaseManager;
import cookbook.repository.ThemesRepository;
import cookbook.repository.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddUserSceneController implements Initializable{

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameField.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                System.out.println("Scene is now set.");
                ThemesRepository.applyTheme(nameField.getScene());
            }
        });
    }

    @FXML
    public void saveClicked(ActionEvent event) {

        DatabaseManager dbManager = new DatabaseManager();
        UserDao userDao = new UserDao(dbManager);

        // save user
        userDao.insertUser(nameField.getText(), usernameField.getText(), passwordField.getText(), isAdminCheckBox.isSelected() ? 1 : 0);

        Stage stage = (Stage) closeButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @FXML
    public void cancelClicked(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
}
