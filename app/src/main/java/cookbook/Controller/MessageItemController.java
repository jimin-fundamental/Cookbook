package cookbook.Controller;

import cookbook.DatabaseManager;
import cookbook.model.Message;
import cookbook.model.Recipe;
import cookbook.repository.MySqlRecipeRepository;
import cookbook.repository.UserDao;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MessageItemController implements Initializable {
    @FXML
    private Label userNameLabel;
    @FXML
    private Label messageContentLabel;
    @FXML
    private Label recipeTitleLabel;
    @FXML
    private VBox messageVBox; // The VBox that holds the message item

    private MySqlRecipeRepository sqlRepos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DatabaseManager dbManager = new DatabaseManager();  // Assuming DatabaseManager has a default constructor or is suitably instantiated
        this.sqlRepos = new MySqlRecipeRepository(dbManager);
    }

    public void setMessageDetails(Message message) {
        userNameLabel.setText(message.getSenderName()); // Assuming getter method exists
        messageContentLabel.setText(message.getMessage());
        recipeTitleLabel.setText(message.getRecipeTitle()); // Assuming getter method exists
    }

    public void openRecipeFromMessage(Message message){
        Recipe recipeFromMessage = new Recipe();
        recipeFromMessage.setName(message.getRecipeTitle());


    }

//    @FXML
//    void messageItemClicked(MouseEvent event) {
//        // Logic to open the recipe page
//        String recipeName = recipeTitleLabel.getText();
//        System.out.println("Recipe " +  recipeName + " clicked by " + userNameLabel.getText());
//        // Implement navigation or event handling here
//
//
//    }
}
