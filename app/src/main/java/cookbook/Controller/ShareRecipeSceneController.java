package cookbook.Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.controlsfx.control.SearchableComboBox;

import cookbook.DatabaseManager;
import cookbook.model.Recipe;
import cookbook.model.User;
import cookbook.repository.MySqlRecipeRepository;
import cookbook.repository.ThemesRepository;
import cookbook.repository.UserDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ShareRecipeSceneController implements Initializable {

    @FXML
    private SearchableComboBox<String> comboBoxSelectUser;
    @FXML
    private Button sendBtn;
    @FXML
    private TextField sendText;

    private MySqlRecipeRepository sqlRepos;
    private UserDao userDao;
    private User sender;
    private User receiverUser;
    private Recipe recipe;
    private List<User> allUsers;

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setUser(User user) {
        this.sender = user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DatabaseManager dbManager = new DatabaseManager();  // Assuming DatabaseManager has a default constructor or is suitably instantiated
        this.sqlRepos = new MySqlRecipeRepository(dbManager);
        userDao = new UserDao(dbManager);

        fetchingUsersComboBox();
        sendBtn.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                System.out.println("Scene is now set.");
                ThemesRepository.applyTheme(sendBtn.getScene());
            }
        });
    }

    private void fetchingUsersComboBox() {
        allUsers = userDao.getAllUser();
        if (allUsers == null) {
            System.out.println("Failed to fetch users from the database.");
            return;
        }
        ObservableList<String> userNames = FXCollections.observableArrayList(
                allUsers.stream()
                        .map(user -> user.getName() + " (" + user.getUserName() + ")")  // Adjust according to actual getters in User class
                        .collect(Collectors.toList())
        );

        comboBoxSelectUser.setItems(userNames);
    }

    @FXML
    public void sendingBtnPressed(ActionEvent actionEvent) {
        String message = sendText.getText();
        String selected = comboBoxSelectUser.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String username = selected.substring(selected.indexOf('(') + 1, selected.length() - 1);
            receiverUser = allUsers.stream().filter(u -> u.getUserName().equals(username)).findFirst().orElse(null);
            if (receiverUser != null) {
                System.out.println("Sending message: " + message + " to " + receiverUser.getName());
                sqlRepos.sharingRecipe(sender, recipe, receiverUser, message);

                // Close the current window completely after sending the message
                Node source = (Node) actionEvent.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }
        }
    }


}
