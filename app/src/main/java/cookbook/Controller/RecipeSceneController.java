package cookbook.Controller;
//handles the detailed view of a recipe

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.io.IOException;
import java.util.function.Consumer;

import cookbook.DatabaseManager;
import cookbook.SceneModifier;
import cookbook.model.Ingredient;
import cookbook.model.Recipe;
import cookbook.model.User;
import cookbook.repository.MySqlRecipeRepository;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ComboBox;

public class RecipeSceneController implements Initializable {

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

    @FXML
    private FontAwesomeIconView star;

    @FXML
    private ComboBox<Integer> servingsComboBox;

    @FXML
    private FlowPane tagsFlowPane;

    @FXML
    private Button removeFromWeeklyListButton;

    private MySqlRecipeRepository recipeRepos;
    private Recipe recipe;
    private User user;
    private List<FXMLLoader> loaders = new ArrayList<>();
    private List<StackPane> tagStacks = new ArrayList<>();
    private List<TagController> controllers = new ArrayList<>();
    private AddTagsButtonController addTagsButtonController = null;
    private int servings;
    private MySqlRecipeRepository sqlRepos = new MySqlRecipeRepository(new DatabaseManager());

    public void setServings(int servings){
        this.servings = servings;
    }

    public void setUser(User user) {
        this.user = user;
        this.recipeRepos = new MySqlRecipeRepository(new DatabaseManager(), user);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }


    public void setRecipeData(Recipe recipe, int numberOfServings) {
        recipeRepos = new MySqlRecipeRepository(new DatabaseManager());
        this.recipe = recipe;
        this.servings = numberOfServings;

        servingsComboBox.setValue(this.servings);
        changeNumberOfServings();


        List<String> tags = sqlRepos.getAllTags(recipe, user);

        // Clear existing tags
        tagsFlowPane.getChildren().clear();

        // Add tags to the FlowPane
        for (String tag : tags) {
            try {
                this.loaders.add(new FXMLLoader(
                        getClass().getResource(
                                "/cookbook.view/Tag.fxml")));
                this.tagStacks.add(this.loaders.get(this.loaders.size() - 1).load());
                // Apply any desired styling to the tag label
                this.controllers.add(this.loaders.get(this.loaders.size() - 1).getController());
                this.controllers.get(this.controllers.size() - 1).setTagName(tag);

                this.tagsFlowPane.getChildren().add(this.tagStacks.get(this.tagStacks.size() - 1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            this.loaders.add(new FXMLLoader(
                    getClass().getResource(
                            "/cookbook.view/AddTagsButton.fxml")));
            this.tagStacks.add(this.loaders.get(this.loaders.size() - 1).load());
            // Apply any desired styling to the tag label
            this.addTagsButtonController = loaders.get(loaders.size() - 1).getController();
            this.addTagsButtonController.initializeData(this.user, this.recipe);

            this.tagsFlowPane.getChildren().add(tagStacks.get(tagStacks.size() - 1));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setStar();

        Integer[] servings = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        for (int index = 0; index < servings.length; index++) {
            servings[index] = servings[index] * this.recipe.getNumberOfPersons();
        }
        //servingsComboBox.setValue(this.recipe.getNumberOfPersons());
        servingsComboBox.getItems().addAll(servings);

        this.recipeNameText.setText(recipe.getName());
        this.recipeDescriptionText.setText(recipe.getShortDescription());
        this.IngredientHeadlineText.setText("Ingredients (" + recipe.getNumberOfPersons() + " servings)");

        // Load image
        String imagePath = recipe.getImagePath() != null ? recipe.getImagePath()
                : "https://images.pexels.com/photos/1109197/pexels-photo-1109197.jpeg";
        Image image = new Image(imagePath, true); // The true parameter allows for asynchronous loading
        recipeImageView.setImage(image);

        // add the missing details to the recipe
        recipeRepos.fetchRecipeDetails(recipe);

        int i = 1;
        // recipe contains certain values and this.recipe different values so you have
        // to differ.
        // There must be a better solution but for now it is that.
        vBoxProcessSteps.getChildren().clear();
        for (String step : recipe.getProcessSteps()) {
            vBoxProcessSteps.getChildren().add(new Text(i++ + ": " + step));
        }
    }

    public void editRecipeScene(ActionEvent event) {
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

    @FXML
    void addToWeeklyList(ActionEvent event) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookbook.view/AddToWeeklyList_new.fxml"));

            // create new stage for new window of the recipe
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(fxmlLoader.load()));

            // get the controller to call the method to set the data
            AddToWeeklyListController controller = fxmlLoader.getController();
            controller.setRecipe(this.recipe);
            controller.setUser(user);
            controller.setServings(servingsComboBox.getValue());

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void changeServings(ActionEvent event) {
       changeNumberOfServings();
        // Refresh the scene to reflect the changes
    }

    @FXML
    void addToFavouritesClicked(MouseEvent event) {
        if (!recipe.getIsFavourite()) {
            recipeRepos.saveToFavorites(recipe, user);
        } else {
            recipeRepos.removeFromFavorites(recipe, user);
        }
        setStar();
    }

    private void setStar() {
        if (recipe.getIsFavourite()) {
            star.setFill(Color.YELLOW);
        } else {
            star.setFill(Color.WHITE);

        }
    }

    private void changeNumberOfServings(){
        Integer ogServings = this.recipe.getNumberOfPersons();
        // Read the new servings from the dropdown menu
        Integer newServings = servingsComboBox.getValue();
        // Code to read the new servings from the dropdown menu
        // Calculate the ratio of new servings to original servings
        double scaleFactor = (double) newServings / (double) ogServings;
        // Create a new ingredients list and multiply ingredients
        List<Ingredient> ingredients = this.recipe.getIngredients();
        List<Ingredient> newIngredients = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            double newAmount = ingredient.getAmount() * scaleFactor;
            Ingredient newIngredient = new Ingredient();
            newIngredient.setName(ingredient.getName());
            newIngredient.setAmount((int) newAmount);
            newIngredient.setUnit(ingredient.getUnit());
            newIngredients.add(newIngredient);
        }
        // Set new servings and ingredients
        ingredientsFlowPane.getChildren().clear();
        for (Ingredient ingredient : newIngredients) {
            ingredientsFlowPane.getChildren().add(
                    new Text(ingredient.getName() + " (" + ingredient.getAmount() + " " + ingredient.getUnit() + ")"));
        }

        // this.recipe.setNumberOfPersons(newServings);
        // this.recipe.setIngredients(newIngredients);
    }

    private void refreshRecipeView() {
        System.out.println("refreshRecipeView");
        // Assuming RecipeView.fxml is the FXML file for the RecipeView page
        try {
            SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook/view/RecipeView.fxml")), null);
            System.out.println("Recipe view refreshed");
        } catch (IOException e) {
            System.out.println("Error refreshing RecipeView: " + e.getMessage());
            e.printStackTrace();

        }

    }
}
