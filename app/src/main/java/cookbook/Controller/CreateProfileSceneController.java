package cookbook.controller;

import cookbook.SceneModifier;
import cookbook.repository.UserDao;
import javafx.scene.Node;

import java.io.IOException;

import cookbook.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateProfileSceneController {

    @FXML
    private Button create_profile_button;

    @FXML
    private TextField name_textbox;

    @FXML
    private TextField pw_textbox;

    @FXML
    private TextField uName_textbox;

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
        String name = name_textbox.getText();
        String userName = uName_textbox.getText();
        String password = pw_textbox.getText();

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


