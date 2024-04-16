package cookbook.Controller;


import com.google.common.cache.CacheLoader.InvalidCacheLoadException;

import cookbook.SceneModifier;
import cookbook.model.Recipe;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RecipeItemController {

    @FXML
    private Text recipeNameText;
    

    private Recipe recipe;

    public void setRecipeData(Recipe recipe){
        this.recipe = recipe;
        recipeNameText.setText(recipe.getName());
        String tags = "";
        for (String tag : recipe.getTags()){
            tags += tag + ", ";
        }
        //tagsLabel.setText(tags);

    }

    // show the clicked recipe with more details
    @FXML
    void recipeClicked(MouseEvent event) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookbook.view/RecipeScene.fxml"));

        // create new stage for new window of the recipe
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(new Scene(fxmlLoader.load()));

        // get the controller to call the method to set the data
        RecipeSceneController controller = fxmlLoader.getController();
        controller.setRecipeData(this.recipe);

        stage.show();

    }

}
