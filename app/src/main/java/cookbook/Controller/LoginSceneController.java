package cookbook.Controller;

import java.io.IOException;

import cookbook.DatabaseManager;

import cookbook.SceneModifier;
import cookbook.repository.UserDao;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LoginSceneController {
    @FXML
    private TextField pwTextbox;

    @FXML
    private Pane scene1;

    @FXML
    private Button signInButton;

    @FXML
    private Button createProfileButton;

    @FXML
    private TextField uNameTextbox;

    private UserDao userDao;

    public LoginSceneController() {
        DatabaseManager dbManager = new DatabaseManager();
        userDao = new UserDao(dbManager);
    }

    @FXML
    void signInButtonPressed(ActionEvent event) throws IOException{
        String username = uNameTextbox.getText();
        String password = pwTextbox.getText();

        // check database for usernamename and password
        if(!userDao.checkUser(username, password)){
            System.out.println("credentials not correct!");
        }
        else{
            changeScene("/cookbook.view/RecipeView.fxml", event);
        }
    }

    @FXML
    void startPageButtonPressed(ActionEvent event) throws IOException{
        changeScene("/cookbook.view/RecipeView.fxml", event);
    }

    @FXML
    void createProfileButtonPressed (ActionEvent event) throws IOException {
        changeScene("/cookbook.view/CreateProfileScene.fxml", event);
    }

    private void changeScene(String fxmlPath, ActionEvent event) throws IOException {
        SceneModifier.change_scene(FXMLLoader.load(getClass().getResource(fxmlPath)), (Stage)((Node)event.getSource()).getScene().getWindow());
    }

}
