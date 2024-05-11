package cookbook.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import cookbook.DatabaseManager;

import cookbook.SceneModifier;
import cookbook.model.User;
import cookbook.repository.UserDao;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoginSceneController implements Initializable{
    @FXML
    private TextField pwTextbox;
    
    @FXML
    private Pane scenePane;
    
    @FXML
    private Button signInButton;
    
    @FXML
    private Button createProfileButton;
    
    @FXML
    private TextField uNameTextbox;
    
    @FXML
    private Label errorLabel;
    
    private UserDao userDao;
    //private List<Object> threadOutput;
    private volatile RecipeViewController controller;
    private volatile Stage stage;
    private Scene scene;
    Thread thread;


    public LoginSceneController() {
        DatabaseManager dbManager = new DatabaseManager();
        userDao = new UserDao(dbManager);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       startRecipeView();
    }
    public void setStage(Stage stage){
        this.stage = stage;
    }
    private void setController(RecipeViewController controller){
        this.controller = controller;
    }

    private void startRecipeView(){
        // Node node = (Node)scenePane;
        // System.out.println(node);;
        // Scene scene;
        // System.out.println(node.getParent());
        // scene = node.getScene();;

        //Scene scene = node.getScene();
        try {
            // if (scene != null) {
               
            //     if (window instanceof Stage) {
                    // Stage stage = (Stage) window;
                    // this.setStage(stage);
                    // Proceed with your stage manipulation logic
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookbook.view/RecipeView.fxml"));
                    Scene newScene = new Scene(fxmlLoader.load());
                    this.scene = newScene;
                    // this.stage.setScene(newScene);
                    RecipeViewController controller = fxmlLoader.getController();
                    this.setController(controller);
                    //returnValues.add(controller);
    
            //     } else {
            //         System.out.println("The window associated with the scene is not a Stage.");
            //     }
            // } else {
            //     System.out.println("The node is not within a scene.");
            // }
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    @FXML
    void signInButtonPressed(ActionEvent event) throws IOException{
        String username = uNameTextbox.getText();
        String password = pwTextbox.getText();

        // check database for usernamename and password 
        User user = userDao.checkUser(username, password);
        if(user == null){
            errorLabel.setVisible(true);
        }
        else{

            try {
                this.controller.setUserName(user);
                this.stage.setScene(this.scene);
                //this.stage.show();

            } catch (Exception e) {
                // TODO: handle exception
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
