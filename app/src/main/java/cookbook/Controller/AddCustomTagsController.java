package cookbook.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import cookbook.DatabaseManager;
import cookbook.model.Recipe;
import cookbook.model.User;
import cookbook.repository.MySqlRecipeRepository;
import cookbook.repository.ThemesRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class AddCustomTagsController implements Initializable{
    @FXML
    private TextArea tagsArea;

    private Recipe recipe;
    private MySqlRecipeRepository sqlRepos;
    private User user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tagsArea.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                System.out.println("Scene is now set.");
                ThemesRepository.applyTheme(tagsArea.getScene());
            }
        });
    }

    public AddCustomTagsController() {
        // Empty constructor required by FXML
    }
    public void setUser(User user) {
        this.user = user;
        this.sqlRepos = new MySqlRecipeRepository(new DatabaseManager(), user);
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    //need to get userId from other method
    @FXML
    public void addCustomTags(ActionEvent event) {
        if (user == null) {
            System.out.println("User not initialized.");
            return;
        }

        String customTag = tagsArea.getText();
        try {
            sqlRepos.addCustomTagsRepo(customTag, user.getId(), recipe);

            // Fetch updated tags
            sqlRepos.getCustomTags(recipe, user);

            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
