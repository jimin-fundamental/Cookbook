package cookbook.Controller;

import java.io.IOException;

import cookbook.model.Recipe;
import cookbook.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AddTagsButtonController {

    private User user;
    private Recipe recipe;

    // MouseEvent
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
            // controller.setRecipe(this.recipe);
            controller.setUser(this.user); // Assuming 'user' is available in this context
            controller.setRecipe(this.recipe);

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
