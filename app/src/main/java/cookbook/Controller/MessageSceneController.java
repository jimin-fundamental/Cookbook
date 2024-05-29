package cookbook.Controller;

import cookbook.repository.ThemesRepository;
import cookbook.DatabaseManager;
import cookbook.model.Message;
import cookbook.model.Recipe;
import cookbook.model.User;
import cookbook.repository.MessageService;
import cookbook.repository.MySqlRecipeRepository;
import cookbook.repository.UserDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.checkerframework.checker.units.qual.s;

public class MessageSceneController implements Initializable {
//    @FXML
//    private VBox messageListVBox;

    @FXML
    private ListView<Message> messageListView;

    private MySqlRecipeRepository sqlRepos;
    private MessageService messageService;
    private UserDao userDao;
    private User user;
    private List<Recipe> recipeList;


    public void setRecipeRepos(MySqlRecipeRepository sqlRepos) {
        this.sqlRepos = sqlRepos;
        DatabaseManager dbManager = new DatabaseManager();  // Assuming DatabaseManager has a default constructor or is suitably instantiated
        // this.sqlRepos = new MySqlRecipeRepository(dbManager);
        userDao = new UserDao(dbManager);
        this.messageService = new MessageService(sqlRepos);
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
        messageListView.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                System.out.println("Scene is now set.");
                ThemesRepository.applyTheme(messageListView.getScene());
            }
        });
    }

    // Manually called initialization
    public void initializeManually() {
        messageListView.setCellFactory(param -> new ListCell<Message>() {
            @Override
            protected void updateItem(Message message, boolean empty) {
                super.updateItem(message, empty);
                if (empty || message == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(message.getSenderName() + ": " + message.getMessage() + " [" + message.getRecipeTitle() + "]");
                }
            }
        });
        messageListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                openRecipeFromMessage(newVal);
            }
        });
        loadMessages();
    }

    private void loadMessages() {
        if (this.user == null) {
            System.out.println("this user is null");
            return;
        }
        // Assuming sqlRepos is already initialized and user is set
        List<Message> messages = sqlRepos.showAllMessage(user);
        messageListView.getItems().setAll(messages);
    }

    private void openRecipeFromMessage(Message message) {
        System.out.println("openRecipeFromMessage"+message.getRecipeTitle());
        try {
            Recipe recipe = messageService.fetchRecipeForMessage(message);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookbook.view/RecipeScene.fxml"));

            // create new stage for new window of the recipe
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
    
            // get the controller to call the method to set the data
            RecipeSceneController controller = fxmlLoader.getController();
            controller.setUser(user);
            controller.setRecipeData(recipe, recipe.getNumberOfPersons());
    
            stage.show();

            // FXMLLoader loader = new FXMLLoader(getClass().getResource("/cookbook.view/RecipScene.fxml"));
            // Parent root = loader.load();

            // RecipeSceneController controller = loader.getController();
            // controller.setRecipeData(recipe, ""); // Make sure Message has a method getRecipe()
            // controller.setUser(user); // Assuming you have a User field in this controller

            // Stage stage = new Stage();
            // stage.setTitle("Recipe Details: " + message.getRecipeTitle());
            // stage.setScene(new Scene(root));
            // stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    private void openMessageItem(Message message) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cookbook.view/MessageItem.fxml"));
//            Parent root = loader.load();
//
//            MessageItemController controller = loader.getController();
//            controller.setMessageDetails(message);
//            controller.openRecipeFromMessage(message);
//
//            Stage stage = new Stage();
//            stage.setTitle("Message Details");
//            stage.setScene(new Scene(root));
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}
