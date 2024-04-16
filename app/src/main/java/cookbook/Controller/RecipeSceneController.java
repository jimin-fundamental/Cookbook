package cookbook.Controller;

import cookbook.model.Recipe;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RecipeSceneController{

    @FXML
    private Text recipeNameText;

    @FXML
    private Text recipeDescriptionText;

    @FXML
    private VBox recipeDetailsVBox;

    public void setRecipeData(Recipe recipe){
        recipeNameText.setText(recipe.getName());
    }

}
