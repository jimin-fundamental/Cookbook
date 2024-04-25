package cookbook.Controller;

// manages the individual items in a list of recipes

import com.google.common.cache.CacheLoader.InvalidCacheLoadException;

import cookbook.SceneModifier;
import cookbook.model.Recipe;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.util.List;

// Display basic recipe information (like name and associated tags).
public class RecipeItemController {

    @FXML
    private Text recipeNameText;

    @FXML
    private Label searchHitsLabel;

    @FXML
    private Pane recipeImagePane;

    @FXML
    private StackPane tagsPane;

    private Recipe recipe;

    public void setRecipeData(Recipe recipe, String tagHits) {
        this.recipe = recipe;
        recipeNameText.setText(recipe.getName());

        // Assuming getImagePath() returns the URL of the image as a string
        String imagePath = recipe.getImagePath() != null ? recipe.getImagePath()
                : "https://images.pexels.com/photos/1109197/pexels-photo-1109197.jpeg";

        recipeImagePane.setStyle("-fx-background-image: url(" + imagePath + "); -fx-background-size: cover;");
        // Setting tags
        String tags = "";
        for (String tag : recipe.getTags()) {
            tags += tag + ", ";
        }
        // tagsLabel.setText(tags);

        searchHitsLabel.setText(tagHits);
        if(tagHits != ""){
            tagsPane.setStyle("-fx-background-color: green; -fx-background-radius: 4;");
        }
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
