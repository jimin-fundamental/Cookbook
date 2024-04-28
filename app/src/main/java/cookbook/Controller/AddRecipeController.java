package cookbook.Controller;

import cookbook.DatabaseManager;
import cookbook.SceneModifier;
import cookbook.repository.MySqlRecipeRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.stage.Stage;

public class AddRecipeController {

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
    private TextField titleField;

    private MySqlRecipeRepository sqlRepos = new MySqlRecipeRepository(new DatabaseManager());


    @FXML
    void addRecipe(ActionEvent event) {
        try{
            // add new Recipe
            sqlRepos.addRecipe(titleField.getText(), shortDescriptionField.getText(), descriptionArea.getText(), imageUrlField.getText(), Integer.parseInt(servingsField.getText()), ingredientsArea.getText(), tagsArea.getText());
            // Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            // stage.close();

            SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook.view/RecipeView.fxml")), (Stage)((Node)event.getSource()).getScene().getWindow());
        
        }
        catch(Exception e){
            // print error message (prototype)
            System.out.println("Error");
        }
        
    }

    @FXML
    void clearFields(ActionEvent event) {
        descriptionArea.setText("");
        imageUrlField.setText("");
        ingredientsArea.setText("");
        servingsField.setText("");
        shortDescriptionField.setText("");
        tagsArea.setText("");
        titleField.setText("");
    }

}
