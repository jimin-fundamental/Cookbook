package cookbook.Controller;

import cookbook.DatabaseManager;
import cookbook.SceneModifier;
import cookbook.repository.MySqlRecipeRepository;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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

    @FXML
    private TextArea tagsArea;

    @FXML
    private CheckComboBox<String> tagsComboBox;

    @FXML
    private TextField titleField;


    private MySqlRecipeRepository sqlRepos = new MySqlRecipeRepository(new DatabaseManager());


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPredeterminedTags();
    }

    private void loadPredeterminedTags() {
        List<String> tags = sqlRepos.getAllPredeterminedTags();
        ObservableList<String> tagList = FXCollections.observableArrayList(tags);
        //tagsComboBox.setItems(tagList);
        // Use setAll to replace any existing items
        tagsComboBox.getItems().setAll(tagList);
    }

    @FXML
    void addRecipe(ActionEvent event) {
        try {
            List<String> selectedTags = tagsComboBox.getCheckModel().getCheckedItems();
            List<String> customTags = Arrays.stream(tagsArea.getText().split(";"))
                    .map(String::trim)
                    .filter(tag -> !tag.isEmpty())
                    .collect(Collectors.toList());

            // Add the recipe and get its ID
            int recipeId = sqlRepos.addRecipeRepo(titleField.getText(), shortDescriptionField.getText(), descriptionArea.getText(),
                    imageUrlField.getText(), Integer.parseInt(servingsField.getText()),
                    ingredientsArea.getText(), selectedTags);

            if (recipeId != -1) {  // Check if the recipe was added successfully
                List<Integer> tagIds = sqlRepos.ensureTagsExist(selectedTags, true);  // For predetermined tags
                tagIds.addAll(sqlRepos.ensureTagsExist(customTags, false));  // For custom tags

                sqlRepos.linkTagsToRecipe(recipeId, tagIds);  // Link all tags to the new recipe

                // Redirect or refresh the UI as necessary
                SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook.view/RecipeView.fxml")),
                        (Stage)((Node)event.getSource()).getScene().getWindow());
            } else {
                System.out.println("Failed to add the recipe.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    void clearFields(ActionEvent event) {
        descriptionArea.setText("");
        imageUrlField.setText("");
        ingredientsArea.setText("");
        servingsField.setText("");
        shortDescriptionField.setText("");
        tagsComboBox.getCheckModel().clearChecks(); // Clear selections in CheckComboBox
        tagsArea.setText("");
        titleField.setText("");
    }

}
