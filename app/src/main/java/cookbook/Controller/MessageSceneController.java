package cookbook.Controller;

import cookbook.DatabaseManager;
import cookbook.model.Message;
import cookbook.model.Recipe;
import cookbook.model.User;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MessageSceneController implements Initializable {
//    @FXML
//    private VBox messageListVBox;

    @FXML
    private ListView<Message> messageListView;

    private MySqlRecipeRepository sqlRepos;
    private UserDao userDao;
    private User user;
    private Recipe recipe;
    private List<Recipe> recipeList;


    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    public void setUser(User user) {
//        this.user = user;
//        // Re-load messages when user is set
//        loadMessages();
//    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DatabaseManager dbManager = new DatabaseManager();  // Assuming DatabaseManager has a default constructor or is suitably instantiated
        this.sqlRepos = new MySqlRecipeRepository(dbManager);
        userDao = new UserDao(dbManager);
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
                openMessageItem(newVal);
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


    private void openMessageItem(Message message) {
//        // Find the recipe from the loaded list
//        Recipe recipeToShow = message.getRecipeId();
//
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cookbook.view/RecipeScene.fxml"));
//            Parent root = loader.load();
//
//            RecipeSceneController controller = loader.getController();
//            controller.setUser(user); // Assuming user is defined in your class
//            controller.setRecipeData(recipeToShow, recipeToShow.getNumberOfPersons());
//
//            Stage stage = new Stage();
//            stage.setTitle(recipeToShow.getName());
//            stage.setScene(new Scene(root));
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }




}
