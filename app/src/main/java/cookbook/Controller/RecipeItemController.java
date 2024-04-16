package cookbook.Controller;

import com.google.common.cache.CacheLoader.InvalidCacheLoadException;

import cookbook.SceneModifier;
import cookbook.model.Recipe;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RecipeItemController {

    @FXML
    private Label recipeNameLabel;
    
    @FXML
    private Label shortDescriptionLabel;

    @FXML
    private Label tagsLabel;

    private Recipe recipe;

    public void setRecipeData(Recipe recipe){
        this.recipe = recipe;
        recipeNameLabel.setText(recipe.getName());
        shortDescriptionLabel.setText(recipe.getShortDescription());
        String tags = "";
        for (String tag : recipe.getTags()){
            tags += tag + ", ";
        }
        tagsLabel.setText(tags);

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
        controller.setRecipeData(this.recipe);

        stage.show();

    }

}
