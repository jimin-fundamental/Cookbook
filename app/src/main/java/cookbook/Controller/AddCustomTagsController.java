package cookbook.Controller;

import cookbook.DatabaseManager;
import cookbook.SceneModifier;
import cookbook.model.Recipe;
import cookbook.model.User;
import cookbook.repository.MySqlRecipeRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AddCustomTagsController{
    @FXML
    private TextArea tagsArea;

    private Recipe recipe;
    private MySqlRecipeRepository sqlRepos;
    private User user;
    private RecipeSceneController recipeSceneController;

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
            List<String> updatedTags = sqlRepos.getAllTags(recipe, user);
            System.out.println("Updated Tags: " + updatedTags);  // Debug log
            recipe.getTags().setAll(updatedTags); // Update the observable list, notifying listeners
            System.out.println("Recipe.getTags: "+ recipe.getTags());

            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }



/*
    private void refreshRecipeView() {
        System.out.println("refreshRecipeView");
        // Assuming RecipeView.fxml is the FXML file for the RecipeView page
        try {
            //SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook.view/RecipeView.fxml")), null);
            //SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook.view/RecipeView.fxml")), (Stage)((Node)event.getSource()).getScene().getWindow());
            //SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook.view/RecipeView.fxml")), recipeSceneStage);

            System.out.println("Recipe view refreshed");
        } catch (IOException e) {
            System.out.println("Error refreshing RecipeView: " + e.getMessage());
            e.printStackTrace();

        }

    }

 */

}
