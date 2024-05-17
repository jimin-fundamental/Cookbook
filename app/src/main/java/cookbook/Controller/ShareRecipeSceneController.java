package cookbook.Controller;

import cookbook.DatabaseManager;
import cookbook.model.Recipe;
import cookbook.model.User;
import cookbook.repository.MySqlRecipeRepository;
import cookbook.repository.UserDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.controlsfx.control.SearchableComboBox;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ShareRecipeSceneController implements Initializable {

    @FXML
    private SearchableComboBox<String> comboBoxSelectUser;
    @FXML
    private Button sendBtn;
    @FXML
    private TextField sendText;

    private MySqlRecipeRepository sqlRepo;
    private UserDao userDao;
    private User sender;
    private User receiverUser;
    private Recipe recipe;
    private List<User> allUsers;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DatabaseManager dbManager = new DatabaseManager();  // Assuming DatabaseManager has a default constructor or is suitably instantiated
        userDao = new UserDao(dbManager);
        //We should get sender and recipe info
//        this.recipe = recipe;
//        sender = User.getUser
        fetchingUsersComboBox();
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
                sqlRepo.sendingMessage(recipe, receiverUser, message);
            }
        }
    }


}
