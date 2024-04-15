package cookbook.Controller;

import com.google.common.cache.CacheLoader.InvalidCacheLoadException;

import cookbook.SceneModifier;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RecipeItemController {

    @FXML
    private Label recipeNameLabel;

    public String selectedRecipe;

    public void setRecipeData(String recipeName){
        recipeNameLabel.setText(recipeName);
    }

    // show the clicked recipe with more details
    @FXML
    void recipeClicked(MouseEvent event) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookbook.view/RecipeScene.fxml"));

        
        // create new stage for new window of the recipe
        Stage stage= new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));
        
        // get the controller to call the method to set the data
        RecipeSceneController controller = fxmlLoader.getController();
        controller.setRecipeData(recipeNameLabel.getText());

        stage.show();

    }

}
