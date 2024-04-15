package cookbook.Controller;

import cookbook.model.Recipe;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RecipeSceneController {

    @FXML
    private Label recipeNameLabel;

    public void setRecipeData(String recipeName){
        recipeNameLabel.setText(recipeName);
    }
}
