    package cookbook.Controller;

    import java.io.File;

    //searching

    import java.io.IOException;
    import java.lang.String;
    import java.net.URL;

    import java.util.List;
    import java.util.ResourceBundle;
    import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import cookbook.DatabaseManager;
import cookbook.Controller.HelpViewSceneController;
import cookbook.SceneModifier;
import cookbook.model.Ingredient;
import cookbook.model.Recipe;
import cookbook.model.ThemePreference;
import cookbook.model.User;
import cookbook.repository.MySqlRecipeRepository;
import cookbook.repository.ThemesRepository;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import cookbook.Controller.AddRecipeController;

    public class RecipeViewController implements Initializable {

        @FXML
        private GridPane recipeContainer;

        @FXML
        private MenuItem changeProfile;

        @FXML
        private MenuItem manageUsers;

        @FXML
        private VBox vBox;

        @FXML
        private TextField searchBar;

        @FXML
        private Label greetingText;

        @FXML
        private FontAwesomeIconView star;

        @FXML
        private Button searchButton;

        private List<Recipe> recipeList;
        private MySqlRecipeRepository recipeRepos;
        private User user;
        private Recipe recipe;
        private boolean favoritesShowing = false;
        private static boolean animationDisplayed = false;


    public void setUserName(User user, boolean showGIF) {
        // Image image = new Image(getClass().getResource("/gif/intro.gif").toString());
        // ImageView imageview = new ImageView(image);
        // Group root = new Group(imageview);
        // Scene oldScene = vBox.getScene();
        // Stage ol = (Stage) vBox.getScene().getWindow();
        // Timeline timeline = new Timeline(
        //         new KeyFrame(Duration.ZERO, e -> {
        //             SceneModifier.change_scene(root,
        //                     ol);
        //         }),
        //         new KeyFrame(Duration.seconds(5), e -> {
        //             ol.setScene(oldScene);
        //         }));
        // timeline.play();

        //make the window visible
        // ((Stage)recipeContainer.getScene().getWindow()).show();
        System.out.println("starting thread now!");
        if(showGIF){
            new Thread(new Runnable() {
    
                @Override
                public void run() {
                    if(!animationDisplayed) {
                        // Get the recipes from the database
                        Image image = new Image(getClass().getResource("/gif/intro.gif").toString());
                        System.out.println("got gif now!");
    
                        ImageView imageview = new ImageView(image);
                        Group root = new Group(imageview);
                        Scene oldScene = vBox.getScene();
                        Stage ol = (Stage) vBox.getScene().getWindow();
    
                        // Initialize and play the timeline
                        Timeline timeline = new Timeline(
                                new KeyFrame(Duration.ZERO, e -> {
                                    SceneModifier.change_scene(root, ol);
                                }),
                                new KeyFrame(Duration.seconds(4.6), e -> {
                                    System.out.println("setting new scene!");
    
                                    ol.hide();
                                    ol.setScene(oldScene);
                                    ol.show();
                                })
                        );
                        timeline.play();
                        animationDisplayed = true;
                    }
    
                }
            }).start();
        }

        this.user = user;
        greetingText.setText("Hi, " + user.getUserName() + "!");

        int number = 0;
        for (Recipe recipe : recipeList) {
            displayRecipeItem(recipe, number++, "");

        }


        // get information about favourite recipes
        recipeRepos.getFavorites(recipeList, user);
        // get information about weekly recipes
        recipeRepos.getRecipeWeeklyDates(recipeList, user);

        recipeRepos.getAllCustomTags(recipeList, user);

        if (user.getIsAdmin() == 0) {
            manageUsers.setVisible(false);
        }

        recipeRepos = new MySqlRecipeRepository(new DatabaseManager(), user, recipeList);

    }

    public void setUser(User user) {
        this.user = user;
        this.recipeRepos = new MySqlRecipeRepository(new DatabaseManager(), user);
    }

    public void update(){
        filterRecipes();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // has to be the first to initialize the repository
        try {
            // get the recipes from the database

            recipeRepos = new MySqlRecipeRepository(new DatabaseManager());

            recipeList = new ArrayList<>();
            recipeRepos.getAllRecipes(recipeList, false);
            searchButton.sceneProperty().addListener((observable, oldScene, newScene) -> {
                if (newScene != null) {
                    System.out.println("Scene is now set.");
                    ThemesRepository.applyTheme(searchButton.getScene());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        }

    @FXML
    void changeProfileClicked(ActionEvent event) throws IOException {
        SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook.view/LoginScene.fxml")),
                (Stage) vBox.getScene().getWindow());
    }

    @FXML
    void changeThemeClicked(ActionEvent event) {
        if (ThemePreference.loadTheme().endsWith("light_theme.css")) {
            ThemePreference.saveTheme("/css/dark_theme.css");
        } else {
            ThemePreference.saveTheme("/css/light_theme.css");
        }
        ThemesRepository.applyTheme(searchButton.getScene());

    }

        @FXML
        void manageUsersClicked(ActionEvent event) throws IOException {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/cookbook.view/UsersScene.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                UsersSceneController usersController = loader.getController(); // Get the controller
                usersController.setUserAndInitialize(user);

                stage.setResizable(false);
                stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        @FXML
        void messageClicked(ActionEvent event) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookbook.view/MessageScene.fxml"));
            Parent root = fxmlLoader.load();

            MessageSceneController controller = fxmlLoader.getController();
            controller.setUser(this.user); // Ensure user is set before the scene is displayed
            controller.setRecipeRepos(this.recipeRepos);
            controller.initializeManually(); // If needed, a method to manually start any processes that depend on user

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

        @FXML
        void helpClicked(ActionEvent event) throws IOException {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/cookbook.view/HelpViewScene.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                HelpViewSceneController helpController = loader.getController(); // Get the controller
                helpController.initializeHelp();

                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @FXML
        void searchButtonClicked(ActionEvent event) {
            filterRecipes();
        }


        @FXML
        void addButtonClicked(ActionEvent event) throws IOException {
            System.out.println("addButtonClicked");
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookbook.view/NewRecipe.fxml"));
                Parent root = fxmlLoader.load();

                // Get the controller and set the user
                AddRecipeController addRecipeController = fxmlLoader.getController();
                addRecipeController.setUser(this.user);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setOnCloseRequest(closeEvent -> {
                    try {
                        FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/cookbook.view/RecipeView.fxml"));
                        Parent root2 = fxmlLoader2.load();

                        RecipeViewController controller = fxmlLoader2.getController();
                        controller.setUserName(this.user, false);

                        Stage stage2 = new Stage();
                        stage2.setScene(new Scene(root2));
                        stage2.setTitle("");
                        stage2.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                stage.setTitle("Add New Recipe");
                stage.setResizable(false);
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


//        @FXML
//        void addButtonClicked(ActionEvent event) throws IOException {
//            System.out.println("addButtonClicked");
//            try {
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookbook.view/NewRecipe.fxml"));
//
//                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//                stage.setOnCloseRequest(closeEvent -> {
//                    try {
//                        FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/cookbook.view/RecipeView.fxml"));
//                        Parent root = fxmlLoader2.load();
//
//                        AddRecipeController controller = fxmlLoader2.getController();
//                        controller.setUser(this.user); // Ensure user is set before the scene is displayed
////                        controller.setRecipeRepos(this.recipeRepos);
//                        controller.initializeManually(); // If needed, a method to manually start any processes that depend on user
//
//                        Stage stage2 = new Stage();
//                        stage2.setScene(new Scene(root));
//                        stage2.show();
//
////                        FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/cookbook.view/RecipeView.fxml"));
////                        Stage s = new Stage();
////                        s.setScene(new Scene(fxmlLoader2.load()));
//////                        RecipeViewController controller = fxmlLoader2.getController();
//////                        controller.setUser(this.user); // Ensure user is set before the scene is displayed
//////                        controller.setUserName(this.user);
////                        AddRecipeController controller2 = fxmlLoader2.getController();
////                        controller2.setUser(this.user);
////                        s.show();
//                    } catch (Exception e) {
//                        // TODO: handle exception
//                    }
//                });
//                stage.setTitle("add new recipe");
//                stage.setResizable(false);
//                stage.setScene(new Scene(fxmlLoader.load()));
//
//                stage.show();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }



        @FXML
        void searchBarKeyTyped(ActionEvent event) {
            filterRecipes();
        }

        private void filterRecipes() {
            String[] searchWords = searchBar.getText().split(", ");
            int number = 0;
            // clear all displayed elements
            recipeContainer.getChildren().clear();
            // iterate through all recipes
            for (Recipe recipe : recipeList) {
                String searchHits = "";
                int numberOfHits = 0;
                System.out.println(recipe.getCustomTags());
                for (String searchWord : searchWords) {
                    boolean hit = false;
                    // check if the search word is in the recipe name
                    if (recipe.getName().toLowerCase().contains(searchWord.toLowerCase())) {
                        hit = true;
                        numberOfHits += 1;
                    }
                    // check if the search word is in the recipe tags
                    for (String tag : recipe.getTags()) {
                        if (tag.toLowerCase().contains(searchWord.toLowerCase())) {
                            if (hit != true)
                                numberOfHits += 1;
                            hit = true;
                            if (!searchWord.isEmpty() && !searchHits.contains(tag)) {
                                searchHits += tag + ", ";
                            }
                        }
                    }
                    for (String tag : recipe.getCustomTags()) {
                        if (tag.toLowerCase().contains(searchWord.toLowerCase())) {
                            if (hit != true)
                                numberOfHits += 1;
                            hit = true;
                            if (!searchWord.isEmpty() && !searchHits.contains(tag)) {
                                searchHits += tag + ", ";
                            }
                        }
                    }
                    // check if the search word is in the recipe ingredients
                    for (Ingredient ingredient : recipe.getIngredients()) {
                        if (ingredient.getName().toLowerCase().contains(searchWord.toLowerCase())) {
                            if (hit != true)
                                numberOfHits += 1;
                            hit = true;
                            if (!searchWord.isEmpty() && !searchHits.contains(ingredient.getName())) {
                                searchHits += ingredient.getName() + ", ";
                            }
                        }
                    }
                }
                // add the recipe if found
                if (numberOfHits >= searchWords.length) {
                    // cut the last ", "
                    if (searchHits.length() >= 3)
                        searchHits = searchHits.substring(0, searchHits.length() - 2);
                    // show selected Item
                    displayRecipeItem(recipe, number++, searchHits);
                }
            }
        }

        @FXML
        void favouritesButtonClicked() {
            if (this.favoritesShowing == true) {
                this.favoritesShowing = false;
                star.setFill(Color.TRANSPARENT);
                searchBar.setText("");
                filterRecipes();

            } else {
                this.favoritesShowing = true;
                star.setFill(Paint.valueOf("#ffbd00"));
                int number = 0;
                // clear all displayed elements
                recipeContainer.getChildren().clear();
                // iterate through all recipes
                for (Recipe recipe : recipeList) {
                    if (recipe.getIsFavourite()) {
                        displayRecipeItem(recipe, number++, "");
                    }
                }
            }
        }

        @FXML
        void openWeeklyLists(ActionEvent event) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookbook.view/WeeklyListsScene_new.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(fxmlLoader.load()));
                WeeklyListsSceneController controller = fxmlLoader.getController();
                controller.setUser(user);
                controller.setRecipeList(recipeList);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void displayRecipeItem(Recipe recipe, int number, String searchHits) {
            try {

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/cookbook.view/RecipeItem.fxml"));
                StackPane recipeBox = fxmlLoader.load();
                RecipeItemController controller = fxmlLoader.getController();
                controller.setRecipeData(recipe, searchHits);
                controller.setUser(user);
                int column = number % 5;
                int row = number / 5 + 1;
                recipeContainer.add(recipeBox, column, row);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
