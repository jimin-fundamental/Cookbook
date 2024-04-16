package cookbook.Controller;

import javafx.beans.binding.Bindings;
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

    public void setRecipeData(String recipeName){
        recipeNameText.setText(recipeName);
    }

}
