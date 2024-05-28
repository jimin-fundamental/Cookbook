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
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


    private MySqlRecipeRepository sqlRepos = new MySqlRecipeRepository(new DatabaseManager());

    private User user;
    private Long userID;

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

//, int userID
    @FXML
    void addRecipe(ActionEvent event) {
        try{
            // Collect selected tags from CheckComboBox
            List<String> selectedTags = tagsComboBox.getCheckModel().getCheckedItems();
//            List<String> customTags = Arrays.stream(tagsArea.getText().split(";"))
//                    .map(String::trim)
//                    .filter(tag -> !tag.isEmpty())
//                    .collect(Collectors.toList());

            System.out.println("start to make allTags String");
            // Combine all tags into a single string
            String allTagsString = selectedTags.stream()
                    .collect(Collectors.joining(";"));
            System.out.println("selectedTags: " + selectedTags);

            // Add new Recipe with userID
            sqlRepos.addRecipeRepo(userID, titleField.getText(), shortDescriptionField.getText(), descriptionArea.getText(), imageUrlField.getText(), Integer.parseInt(servingsField.getText()), user.getId(), ingredientsArea.getText(), allTagsString);


            // Close the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

            // Load and show the refreshed RecipeView page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cookbook.view/RecipeView.fxml"));
            SceneModifier.change_scene(loader.load(), new Stage());

//            // Change scene or close window
//            SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook.view/RecipeView.fxml")), (Stage)((Node)event.getSource()).getScene().getWindow());

        }
        catch(Exception e){
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            showAlert("Failed to add recipe. Please try again.");
        }
    }




//    @FXML
//    void addRecipe(ActionEvent event) {
//        try{
//            // Collect selected tags from CheckComboBox
//            List<String> selectedTags = tagsComboBox.getCheckModel().getCheckedItems();
//            List<String> customTags = Arrays.stream(tagsArea.getText().split(";"))
//                    .map(String::trim)
//                    .filter(tag -> !tag.isEmpty())
//                    .collect(Collectors.toList());
//
//            System.out.println("start to make allTags String");
//            // Combine all tags into a single string
//            String allTagsString = Stream.concat(selectedTags.stream(), customTags.stream())
//                    .collect(Collectors.joining(";"));//changed!!!
//            System.out.println("allTagsString: " + allTagsString);
//
////            String tagString = String.join(";", selectedTags); // Join tags into a single string separated by semicolons
//
//            // add new Recipe
//            sqlRepos.addRecipeRepo(titleField.getText(), shortDescriptionField.getText(), descriptionArea.getText(), imageUrlField.getText(), Integer.parseInt(servingsField.getText()), ingredientsArea.getText(), allTagsString);
//
//            // Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//            // stage.close();
//
//            // Change scene or close window
//            SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook.view/RecipeView.fxml")), (Stage)((Node)event.getSource()).getScene().getWindow());
//
//        }
//        catch(Exception e){
//            System.out.println("Error: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//    }

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
