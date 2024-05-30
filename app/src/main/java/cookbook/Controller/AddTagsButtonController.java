package cookbook.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import cookbook.model.Recipe;
import cookbook.model.User;
import cookbook.repository.ThemesRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AddTagsButtonController implements Initializable {

    private User user;
    private Recipe recipe;

    @FXML
    private StackPane addTagsButtonPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addTagsButtonPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                System.out.println("Scene is now set.");
                ThemesRepository.applyTheme(addTagsButtonPane.getScene());
            }
        });
    }

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
