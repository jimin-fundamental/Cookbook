package cookbook.Controller;


import java.io.IOException;
import java.lang.String;
import java.net.URL;

import java.util.List;
import java.util.ResourceBundle;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.stage.Stage;
import cookbook.DatabaseManager;
import cookbook.SceneModifier;
import cookbook.model.Ingredient;
import cookbook.model.Recipe;
import cookbook.repository.MySqlRecipeRepository;

public class RecipeViewController implements Initializable{

    @FXML
    private GridPane recipeContainer;

    @FXML
    private MenuItem changeProfiles;
    
    @FXML
    private VBox vBox;

    @FXML
    private TextField searchBar;

    @FXML
    private Button searchButton;

    private List<Recipe> recipeList;
    private MySqlRecipeRepository recipeRepos;

    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //has to be the first to initialize the repository
        recipeRepos = new MySqlRecipeRepository(new DatabaseManager());
        
        recipeList = recipeRepos.getAllRecipes();
        int number = 0;
        for (Recipe recipe : recipeList){
           displayRecipeItem(recipe, number++, "");
            
        }
    }

    @FXML
    void changeProfileClicked(ActionEvent event) throws IOException {
        SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook.view/LoginScene.fxml")), (Stage)vBox.getScene().getWindow());
    }

    
    @FXML
    void searchButtonClicked(ActionEvent event) {
        filterRecipes();
    }
    
    @FXML
    void searchBarKeyTyped(ActionEvent event) {
        filterRecipes();
    }

    private void filterRecipes(){
        String searchWord = searchBar.getText();
        int number = 0;
        // clear all displayed elements
        recipeContainer.getChildren().clear();

        // iterate through all recipes 
        for (Recipe recipe : recipeList){
            boolean hit = false;
            String searchHits = "";
            // check if the search word is in the recipe name
            if(recipe.getName().toLowerCase().contains(searchWord.toLowerCase())){
                hit = true;
            }
             // check if the search word is in the recipe tags
            for (String tag : recipe.getTags()){
                if( tag.toLowerCase().contains(searchWord.toLowerCase())){
                    hit = true;
                    if(!searchWord.isEmpty()){
                        searchHits += tag + ", ";
                    }
                }
            }
             // check if the search word is in the recipe ingredients
            for (Ingredient ingredient : recipe.getIngredients()){
                if( ingredient.getName().toLowerCase().contains(searchWord.toLowerCase())){
                    hit = true;
                    if(!searchWord.isEmpty())
                        searchHits += ingredient.getName() + ", ";
                    }
            }
            // add the recipe if found 
            if (hit){
                // cut the last ", "
                if (searchHits.length() >= 3)
                    searchHits = searchHits.substring(0, searchHits.length() - 2);
                // show selected Item
                displayRecipeItem(recipe, number++, searchHits);
            }
        }
    } 

    private void displayRecipeItem(Recipe recipe, int number, String searchHits){
        try{
            
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/cookbook.view/RecipeItem.fxml"));
            StackPane recipeBox = fxmlLoader.load();
            RecipeItemController controller = fxmlLoader.getController();
            controller.setRecipeData(recipe, searchHits);
            int column = number % 3;
            int row = number / 3 +1;
            recipeContainer.add(recipeBox, column, row);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
