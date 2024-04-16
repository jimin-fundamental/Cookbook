package cookbook.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RecipeItemController {

    @FXML
    private Text recipeNameText;

    public String selectedRecipe;

    public void setRecipeData(String recipeName) {
        recipeNameText.setText(recipeName);
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
        controller.setRecipeData(recipeNameText.getText());

        stage.show();

    }

}
