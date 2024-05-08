package cookbook.Controller;

import java.io.IOException;

import cookbook.model.Recipe;
import cookbook.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AddTagsButtonController {
    @FXML
    private Label tagsLabel;

    private User user;
    private Recipe recipe;

    //MouseEvent
    @FXML
    void addCustomTags(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookbook.view/AddCustomTags.fxml"));

            // create new stage for new window of the recipe
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(fxmlLoader.load()));

            // get the controller to call the method to set the data
            AddCustomTagsController controller = fxmlLoader.getController();
            controller.setUser(this.user);  // Assuming 'user' is available in this context
            controller.setRecipe(this.recipe);
            /*
            controller.setOnCustomTagsAdded(v -> this.refreshRecipeView());
            controller.setRecipeSceneStage((Stage) ((Node) event.getSource()).getScene().getWindow()); // Pass the Stage object of the RecipeScene
            stage.showAndWait();
             */

            // Set callback to refresh RecipeView after adding custom tags
//            controller.setOnCustomTagsAdded(new Consumer<Void>() {
//                @Override
//                public void accept(Void v) {
//                    refreshRecipeView();
//                }
//            });

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeData(User user, Recipe recipe) {
        this.recipe = recipe;
        this.user = user;
    }

}
