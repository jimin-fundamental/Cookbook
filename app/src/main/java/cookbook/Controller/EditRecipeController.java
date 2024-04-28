package cookbook.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import cookbook.DatabaseManager;
import cookbook.repository.MySqlRecipeRepository;

public class EditRecipeController {
    @FXML
    private TextField recipeId;
    
    @FXML
    private TextField titleField;

    @FXML
    private TextField shortDescriptionField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextField imageUrlField;

    @FXML
    private TextField servingsField;

    @FXML
    private TextArea ingredientsArea;

    @FXML
    private TextArea tagsArea;

    private MySqlRecipeRepository recipeRepository;

    public EditRecipeController() {
        DatabaseManager dbManager = new DatabaseManager();
        recipeRepository = new MySqlRecipeRepository(dbManager);
    }

    @FXML
    private void updateRecipe() {
        long id = Integer.parseInt(recipeId.getText());
        String name = titleField.getText();
        String shortDescription = shortDescriptionField.getText();
        String description = descriptionArea.getText();
        String imageUrl = imageUrlField.getText();
        int servings = Integer.parseInt(servingsField.getText());
        String ingredientsText = ingredientsArea.getText();
        String tagsText = tagsArea.getText();

        recipeRepository.updateRecipe(id, name, shortDescription, description, imageUrl, servings, ingredientsText, tagsText);
        
    }

   
}
