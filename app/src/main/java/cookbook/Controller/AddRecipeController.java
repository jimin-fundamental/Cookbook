package cookbook.Controller;

import cookbook.DatabaseManager;
import cookbook.SceneModifier;
import cookbook.model.Recipe;
import cookbook.model.User;
import cookbook.repository.MySqlRecipeRepository;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.stage.Window;
import org.controlsfx.control.CheckComboBox;

public class AddRecipeController implements Initializable {

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextField imageUrlField;

    @FXML
    private TextArea ingredientsArea;

    @FXML
    private TextField servingsField;

    @FXML
    private TextField shortDescriptionField;

//    @FXML
//    private TextArea tagsArea;

    @FXML
    private CheckComboBox tagsComboBox;

    @FXML
    private TextField titleField;

    @FXML
    private ScrollPane addscenePane;



    private MySqlRecipeRepository sqlRepos = new MySqlRecipeRepository(new DatabaseManager());

    private User user;
    private Long userID;
    private volatile RecipeViewController controller;
    private Scene scene;

    public void setUser(User user){
        this.user = user;
        userID =user.getId();
        System.out.println("User ID: " + userID);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPredeterminedTags();
//        loadCustomTags();
    }

//    private void loadCustomTags() {
//        List<String> customtags = sqlRepos.getAllPredeterminedTags();
//        ObservableList<String> tagList = FXCollections.observableArrayList(customtags);
//        tagsComboBox.getItems().setAll(tagList);
//    }
//
    private void loadPredeterminedTags() {
        List<String> tags = sqlRepos.getAllPredeterminedTags();
        ObservableList<String> tagList = FXCollections.observableArrayList(tags);
        tagsComboBox.getItems().setAll(tagList);
    }

    public void initializeManually() {
        // If you need to load custom tags or other data that depends on the user
        loadPredeterminedTags();
    }

    private void setController(RecipeViewController controller){
        this.controller = controller;
    }


//    @FXML
//    void addRecipe(ActionEvent event) {
//        try{
//            // Collect selected tags from CheckComboBox
//            List<String> selectedTags = tagsComboBox.getCheckModel().getCheckedItems();
////            List<String> customTags = Arrays.stream(tagsArea.getText().split(";"))
////                    .map(String::trim)
////                    .filter(tag -> !tag.isEmpty())
////                    .collect(Collectors.toList());
//
//            System.out.println("start to make allTags String");
//            // Combine all tags into a single string
//            String allTagsString = selectedTags.stream()
//                    .collect(Collectors.joining(";"));
//            System.out.println("selectedTags: " + selectedTags);
//
//            // Add new Recipe with userID
//            sqlRepos.addRecipeRepo(userID, titleField.getText(), shortDescriptionField.getText(), descriptionArea.getText(), imageUrlField.getText(), Integer.parseInt(servingsField.getText()), user.getId(), ingredientsArea.getText(), allTagsString);
//
//
//            // Close the current stage
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            stage.close();
//
//            // Load and show the refreshed RecipeView page
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cookbook.view/RecipeView.fxml"));
//            SceneModifier.change_scene(loader.load(), new Stage());
//
////            // Change scene or close window
////            SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook.view/RecipeView.fxml")), (Stage)((Node)event.getSource()).getScene().getWindow());
//
//        }
//        catch(Exception e){
//            System.out.println("Error: " + e.getMessage());
//            e.printStackTrace();
//            showAlert("Failed to add recipe. Please try again.");
//        }
//    }


    @FXML
    void addRecipe(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookbook.view/RecipeView.fxml"));
            Scene newScene = new Scene(fxmlLoader.load());
            this.scene = newScene;
            RecipeViewController controller = fxmlLoader.getController();
            this.setController(controller);

            // Collect input values
            List<String> selectedTags = tagsComboBox.getCheckModel().getCheckedItems();
            String allTagsString = selectedTags.stream().collect(Collectors.joining(";"));

            // Add new Recipe with collected data
            sqlRepos.addRecipeRepo(userID, titleField.getText(), shortDescriptionField.getText(),
                    descriptionArea.getText(), imageUrlField.getText(),
                    Integer.parseInt(servingsField.getText()), user.getId(),
                    ingredientsArea.getText(), allTagsString);

            Node node = (Node) addscenePane;
            Scene scene = node.getScene();

            if (scene != null) {
                Window window = scene.getWindow();
                if (window instanceof Stage) {
                    Stage stage = (Stage) window;

                    stage.setScene(this.scene);

                    this.controller.setUserName(user);
                    stage.show();

                } else {
                    System.out.println("The window associated with the scene is not a Stage.");
                }
            } else {
                System.out.println("The node is not within a scene.");
            }


        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to add recipe. Please try again.");
        }
    }




    private void showAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }


    @FXML
    void clearFields(ActionEvent event) {
        descriptionArea.setText("");
        imageUrlField.setText("");
        ingredientsArea.setText("");
        servingsField.setText("");
        shortDescriptionField.setText("");
        tagsComboBox.getCheckModel().clearChecks(); // Clear selections in CheckComboBox
//        tagsArea.setText("");
        titleField.setText("");
    }


    // create the data to show in the CheckComboBox
}
