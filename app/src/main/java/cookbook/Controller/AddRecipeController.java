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

    @FXML
    private TextArea tagsArea;

    @FXML
    private CheckComboBox tagsComboBox;

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
        tagsComboBox.getItems().setAll(tagList);
    }


    @FXML
    void addRecipe(ActionEvent event) {
        try{
            // Collect selected tags from CheckComboBox
            List<String> selectedTags = tagsComboBox.getCheckModel().getCheckedItems();
            List<String> customTags = Arrays.stream(tagsArea.getText().split(";"))
                    .map(String::trim)
                    .filter(tag -> !tag.isEmpty())
                    .collect(Collectors.toList());

            System.out.println("start to make allTags String");
            // Combine all tags into a single string
            String allTagsString = Stream.concat(selectedTags.stream(), customTags.stream())
                    .collect(Collectors.joining(","));//changed!!!
            System.out.println("allTagsString: " + allTagsString);

//            String tagString = String.join(";", selectedTags); // Join tags into a single string separated by semicolons

            // add new Recipe
            sqlRepos.addRecipeRepo(titleField.getText(), shortDescriptionField.getText(), descriptionArea.getText(), imageUrlField.getText(), Integer.parseInt(servingsField.getText()), ingredientsArea.getText(), allTagsString);

            // Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            // stage.close();

            // Change scene or close window
            SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook.view/RecipeView.fxml")), (Stage)((Node)event.getSource()).getScene().getWindow());

        }
        catch(Exception e){
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


    // create the data to show in the CheckComboBox
}
