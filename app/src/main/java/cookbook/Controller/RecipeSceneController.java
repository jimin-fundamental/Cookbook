package cookbook.Controller;
//handles the detailed view of a recipe

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import cookbook.DatabaseManager;
import cookbook.SceneModifier;
import cookbook.model.Ingredient;
import cookbook.model.Recipe;
import cookbook.model.User;
import cookbook.repository.MySqlRecipeRepository;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import javafx.beans.binding.Bindings;
import javafx.application.Platform;

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
    private StackPane addTagsButton;

    @FXML
    private AddTagsButtonController addTagsButtonController;

    @FXML
    private Button removeFromWeeklyListButton;

    private MySqlRecipeRepository recipeRepos;
    private Recipe recipe;
    private User user;
    private MySqlRecipeRepository sqlRepos = new MySqlRecipeRepository(new DatabaseManager());
    private boolean tagsListenerAdded = false;

    public void setUser(User user) {
        this.user = user;
        this.recipeRepos = new MySqlRecipeRepository(new DatabaseManager(), user);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setRecipeData(Recipe recipe, int numberOfServings) {
        this.recipe = recipe;

        recipeRepos = new MySqlRecipeRepository(new DatabaseManager(), user);
        initializeServings(numberOfServings);

        // Fetch tags and initialize tags view
        fetchAndInitializeTags();

        initializeUIFromRecipe();
    }

    private void fetchAndInitializeTags() {
        // Fetch tags from the repository
        sqlRepos.getAllTags(recipe, user);

        initializeTagsView();
        initializeTagsView();
    }

    private void initializeTagsView() {
        System.out.println("initializeTagsView");
        tagsFlowPane.getChildren().clear(); // Clear existing tags
        System.out.println("removed existing tags by initializeTagsView method");

        // Add all current tags from the recipe's observable list
        recipe.getTags().forEach(this::addTagToPane);
        recipe.getCustomTags().forEach(this::addCustomTagToPane);

        // Add the button for adding new tags at the end
        addAddTagsButton();

        // Setup listener for future updates
        setupTagChangeListener();
    }

    private void setupTagChangeListener() {
        if (!tagsListenerAdded) {
            // Take an initial snapshot of the tags list
            final List<String> oldTags = new ArrayList<>(recipe.getTags());

            recipe.getTags().addListener((ListChangeListener.Change<? extends String> change) -> {
                Platform.runLater(() -> {
                    // Take a new snapshot after changes
                    List<String> newTags = new ArrayList<>(recipe.getTags());

                    // Determine removed tags by checking what's in oldTags but not in newTags
                    List<String> removedTags = oldTags.stream()
                            .filter(tag -> !newTags.contains(tag))
                            .collect(Collectors.toList());

                    // Determine added tags by checking what's in newTags but not in oldTags
                    List<String> addedTags = newTags.stream()
                            .filter(tag -> !oldTags.contains(tag))
                            .collect(Collectors.toList());

                    // Log changes for debugging
                    System.out.println("Removed Tags: " + removedTags);
                    System.out.println("Added Tags: " + addedTags);

                    // Update the UI for removed tags
                    removedTags.forEach(this::removeCustomTagFromPane);

                    // Update the UI for added tags
                    addedTags.forEach(this::addCustomTagToPane);

                    // Update oldTags to reflect the current state
                    oldTags.clear();
                    oldTags.addAll(newTags);
                });
            });
            tagsListenerAdded = true;
        }
    }

    private void addTagToPane(String tag) {
        System.out.println("addTagToPane");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cookbook.view/Tag.fxml"));
            Node tagNode = loader.load(); // Load the FXML
            TagController tagController = loader.getController(); // Get the controller
            tagController.setTagName(tag); // Set the tag name
            tagNode.setUserData(tagController); // Store the controller in userData for later retrieval
            tagsFlowPane.getChildren().add(0, tagNode); // Add to the FlowPane
        } catch (IOException e) {
            System.out.println("Error loading tag view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addCustomTagToPane(String tag) {
        System.out.println("addTagToPane");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cookbook.view/Tag.fxml"));
            Node tagNode = loader.load(); // Load the FXML
            TagController tagController = loader.getController(); // Get the controller
            tagController.setCustom();
            tagController.setTagName(tag); // Set the tag name
            tagNode.setUserData(tagController); // Store the controller in userData for later retrieval
            tagsFlowPane.getChildren().add(0, tagNode); // Add to the FlowPane
        } catch (IOException e) {
            System.out.println("Error loading tag view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void removeCustomTagFromPane(String tag) {
        System.out.println("removeTagFromPane");
        tagsFlowPane.getChildren().removeIf(node -> {
            TagController controller = (TagController) node.getUserData(); // Retrieve the controller from userData
            return controller != null && controller.getTagName().equals(tag); // Check if this is the tag to remove
        });
    }

    private void addAddTagsButton() {

        addTagsButtonController.initializeData(user, recipe);
    }

    private void initializeServings(int numberOfServings) {
        servingsComboBox.getItems().clear();
        Integer[] baseServings = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        ObservableList<Integer> adjustedServings = FXCollections.observableArrayList();
        for (int base : baseServings) {
            adjustedServings.add(base * recipe.getNumberOfPersons());
        }
        servingsComboBox.setItems(adjustedServings);
        servingsComboBox.setValue(numberOfServings);
        changeNumberOfServings();
    }

    private void initializeUIFromRecipe() {
        recipeNameText.setText(recipe.getName());
        recipeDescriptionText.setText(recipe.getShortDescription());
        recipeImageView.setImage(new Image(
                recipe.getImagePath() != null ? recipe.getImagePath()
                        : "https://images.pexels.com/photos/1109197/pexels-photo-1109197.jpeg",
                true));
        vBoxProcessSteps.getChildren().setAll(
                recipe.getProcessSteps().stream().map(step -> new Text(step)).collect(Collectors.toList()));
        setStar();
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

    private void changeNumberOfServings() {
        Integer ogServings = this.recipe.getNumberOfPersons();
        // Read the new servings from the dropdown menu
        Integer newServings = servingsComboBox.getValue();
        IngredientHeadlineText.setText("Ingredients (" + newServings + " servings)");
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
    }
}
