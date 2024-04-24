package cookbook.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;

import cookbook.DatabaseManager;
import cookbook.model.Ingredient;
import cookbook.model.Recipe;
import cookbook.repository.MySqlRecipeRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RecipeSceneController implements Initializable{


    @FXML
    private Text recipeNameText;

    @FXML
    private Text recipeDescriptionText;

    @FXML
    private VBox recipeDetailsVBox;

    @FXML
    private VBox vBoxProcessSteps;
    
    @FXML
    private FlowPane ingredientsFlowPane;

    @FXML
    private Text IngredientHeadlineText;

    private MySqlRecipeRepository recipeRepos;

    public void setRecipeData(Recipe recipe){
        recipeRepos = new MySqlRecipeRepository(new DatabaseManager());
        
        this.recipeNameText.setText(recipe.getName());
        this.recipeDescriptionText.setText(recipe.getShortDescription());
        this.IngredientHeadlineText.setText("Ingredients (" + recipe.getNumberOfPersons() + " servings)");
        

        // add the missing details to the recipe
        recipeRepos.fetchRecipeDetails(recipe);
        
        int i = 1;
        // recipe contains certain values and this.recipe different values so you have to differ.
        // There must be a better solution but for now it is that.
        for (String step : recipe.getProcessSteps()){
            vBoxProcessSteps.getChildren().add(new Text(i++ + ": " + step));
        }
        
        for (Ingredient ingredient : recipe.getIngredients()){
            ingredientsFlowPane.getChildren().add(new Text(ingredient.getName() +" (" + ingredient.getAmount() + " " + ingredient.getUnit() + ")"));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void editRecipeScene(ActionEvent eventS){
        try {
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookbook.view/EditRecipe.fxml"));
            //setRecipeData(recipe);

            Stage stage = (Stage) recipeNameText.getScene().getWindow(); // Get the current stage
            stage.setTitle("edit recipe");
           
            stage.setScene(new Scene(fxmlLoader.load()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
