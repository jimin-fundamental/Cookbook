package cookbook.Controller;

import cookbook.SceneModifier;
import cookbook.repository.UserDao;

import java.io.IOException;

import cookbook.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

public class CreateProfileSceneController {

    @FXML
    private Button createProfileButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField nameTextbox;

    @FXML
    private TextField pwTextbox;

    @FXML
    private TextField unameTextbox;

    private UserDao userDao;

    public CreateProfileSceneController() {
        DatabaseManager dbManager = new DatabaseManager();
        userDao = new UserDao(dbManager);
    }

    @FXML
    void backButtonPressed(ActionEvent event) throws IOException {
        changeScene("/cookbook.view/LoginScene.fxml", event);
    }

    @FXML
    void createProfileButtonPressed(ActionEvent event) throws IOException{
        String name = nameTextbox.getText();
        String userName = unameTextbox.getText();
        String password = pwTextbox.getText();

        // store data in the database
        if (!userDao.insertUser(name, userName, password)) {
            System.out.println("Something went wrong!");
        } else {
            changeScene("/cookbook.view/LoginScene.fxml", event);
        }
    }

    private void changeScene(String fxmlPath, ActionEvent event) throws IOException {
        SceneModifier.change_scene(FXMLLoader.load(getClass().getResource(fxmlPath)), (Stage)((Node)event.getSource()).getScene().getWindow());
    }

//        DatabaseManager manager = new DatabaseManager();
//        if(!manager.insert_user(name, userName, password)){
//            System.out.println("something went wrong!");
//        }
//        else{
//            SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook.view/LoginScene.fxml")), (Stage)((Node)event.getSource()).getScene().getWindow());
//        }
        

}


