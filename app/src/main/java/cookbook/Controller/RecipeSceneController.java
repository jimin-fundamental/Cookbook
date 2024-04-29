package cookbook.Controller;
//handles the detailed view of a recipe

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    @FXML
    private ImageView recipeImageView;

    private MySqlRecipeRepository recipeRepos;
    private Recipe recipe;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setRecipeData(Recipe recipe){
        recipeRepos = new MySqlRecipeRepository(new DatabaseManager());
        this.recipe = recipe;
        
        this.recipeNameText.setText(recipe.getName());
        this.recipeDescriptionText.setText(recipe.getShortDescription());
        this.IngredientHeadlineText.setText("Ingredients (" + recipe.getNumberOfPersons() + " servings)");


        // Load image
        String imagePath = recipe.getImagePath() != null ? recipe.getImagePath() : "https://images.pexels.com/photos/1109197/pexels-photo-1109197.jpeg";
        Image image = new Image(imagePath, true);  // The true parameter allows for asynchronous loading
        recipeImageView.setImage(image);


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



    public void editRecipeScene(ActionEvent event){
        try {
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookbook.view/ChangeRecipe.fxml"));

            // create new stage for new window of the recipe
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(fxmlLoader.load()));


            // get the controller to call the method to set the data
            ChangeRecipeController controller = fxmlLoader.getController();
            controller.setRecipe(this.recipe);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
