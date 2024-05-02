package cookbook.Controller;

import cookbook.DatabaseManager;
import cookbook.SceneModifier;
import cookbook.model.Recipe;
import cookbook.model.User;
import cookbook.repository.MySqlRecipeRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AddCustomTagsController {
    @FXML
    private TextArea tagsArea;

    private Recipe recipe;
    private MySqlRecipeRepository sqlRepos;
    private User user;

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

        //tags in form of tag1;tag2;tag3
        String customTags = tagsArea.getText();
        System.out.println("custom tags: " + customTags);

        try{
            // Add new Recipe with userID
            sqlRepos.addCustomTagsRepo(customTags, user.getId(), recipe);

            // close window
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
//            SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook.view/RecipeScene.fxml")), (Stage)((Node)event.getSource()).getScene().getWindow());

        }
        catch(Exception e){
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

    }

}
