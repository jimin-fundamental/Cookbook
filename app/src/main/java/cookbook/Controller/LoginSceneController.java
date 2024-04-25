package cookbook.Controller;

import java.io.IOException;

import cookbook.DatabaseManager;

import cookbook.SceneModifier;
import cookbook.repository.UserDao;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

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

    @FXML
    private Label errorLabel;

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
            errorLabel.setVisible(true);
        }
        else{

            Node node = (Node) event.getSource();
            Scene scene = node.getScene();

            if (scene != null) {
                Window window = scene.getWindow();
                if (window instanceof Stage) {
                    Stage stage = (Stage) window;
                    // Proceed with your stage manipulation logic
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookbook.view/RecipeView.fxml"));
                    Scene newScene = new Scene(fxmlLoader.load());
                    stage.setScene(newScene);
                    RecipeViewController controller = fxmlLoader.getController();
                    controller.setUserName(username);
                    stage.show();
                } else {
                    System.out.println("The window associated with the scene is not a Stage.");
                }
            } else {
                System.out.println("The node is not within a scene.");
            }
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
