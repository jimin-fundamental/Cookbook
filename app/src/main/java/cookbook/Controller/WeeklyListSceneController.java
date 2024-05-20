package cookbook.Controller;

import java.io.IOException;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import org.controlsfx.glyphfont.INamedCharacter;

import com.mysql.cj.protocol.x.SyncFlushDeflaterOutputStream;

import cookbook.repository.MySqlRecipeRepository;
import cookbook.DatabaseManager;
import cookbook.model.Ingredient;
import cookbook.model.Recipe;
import cookbook.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class WeeklyListSceneController {
    @FXML
    private Text weeklyListTitle;

    @FXML
    private VBox mondayItemContainer;

    @FXML
    private VBox tuesdayItemContainer;

    @FXML
    private VBox wednesdayItemContainer;

    @FXML
    private VBox thursdayItemContainer;

    @FXML
    private VBox fridayItemContainer;

    @FXML
    private VBox saturdayItemContainer;

    @FXML
    private VBox sundayItemContainer;

    @FXML
    private VBox vBox;
    
    private List<Recipe> recipeList;
    private User user;
    private List<WeeklyViewItemController> controllers;
    private int week;
    private WeeklyListsSceneController weeklyListsSceneController;
    private int numberOfEntries;
    private MySqlRecipeRepository recipeRepos;

    public void setUser(User user){
        this.user = user;
        recipeRepos = new MySqlRecipeRepository(new DatabaseManager(), user);
    }

    public void setListsController(WeeklyListsSceneController controller){
        this.weeklyListsSceneController = controller;
        if(weeklyListsSceneController == null){
            System.out.println("controller is null");
        }
        System.out.println("controller is set");
    }

    @FXML
    private void deleteListClicked(ActionEvent event){
        int y = controllers.size();
        for(int i = 0; i < y; i++){
            WeeklyViewItemController controller = controllers.get(0);
            System.out.println("is removing...");
            controller.removeWeekly();
        }
        // for(WeeklyViewItemController controller : controllers){
        //     System.out.println("is removing...");
        //     controller.removeWeekly();
        // }
    }

    @FXML
    private void shoppingListClicked(ActionEvent event){
        try {
            List<Ingredient> ingredients = new ArrayList<>();
            for (Recipe recipe : recipeList){
                for (Map.Entry<Date, Integer> entry : recipe.getWeeklyDates().entrySet()){
                    LocalDate localDate = entry.getKey().toLocalDate();
                    WeekFields weekFields = WeekFields.of(Locale.getDefault());
                    int weekOfYear = localDate.get(weekFields.weekOfWeekBasedYear());
                    if(weekOfYear == this.week){
                        List<Ingredient> temp = recipe.getIngredients();
                        for(Ingredient i : temp){
                            Ingredient ingredient = new Ingredient();
                            ingredient.setName(i.getName());
                            ingredient.setUnit(i.getUnit());
                            ingredient.setAmount(i.getAmount() * (entry.getValue()/recipe.getNumberOfPersons()));
                            ingredients.add(ingredient);
                        }
                    }
                }
            }
            
            for (int i = 0; i < ingredients.size(); i++){
                for (int j = 0; j < ingredients.size(); j++){
                    Ingredient ingredient = ingredients.get(i);
                    Ingredient checkIngredient = ingredients.get(j);
                    if(ingredient.getName() == checkIngredient.getName() && ingredient.getUnit() == checkIngredient.getUnit() && !(ingredient.equals(checkIngredient))){
                        System.out.println(ingredient.getName());
                        ingredient.setAmount(ingredient.getAmount() + checkIngredient.getAmount());
                        ingredients.remove(checkIngredient);
                        // decrement counters because one element was removed
                        if(i > 0){
                            i--;
                        }
                        if(j > 0){
                            j--;
                        }                           
                    }
                }
            }

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookbook.view/ShoppingListScene.fxml"));
            // create new stage for new window of the recipe
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(fxmlLoader.load()));
            ShoppingListSceneController controller = fxmlLoader.getController();
            controller.setUser(user);
            controller.setTitle(week, this.weeklyListTitle.getText());
            // setIngredients has to be done last
            controller.setIngredients(ingredients);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRecipes(String weeklyListTitle, int week, List<Recipe> recipeList) {
        this.controllers = new ArrayList<WeeklyViewItemController>();
        this.recipeList = recipeList;
        this.week = week;
        this.weeklyListTitle.setText(weeklyListTitle);
        mondayItemContainer.getChildren().clear();
        tuesdayItemContainer.getChildren().clear();
        wednesdayItemContainer.getChildren().clear();
        thursdayItemContainer.getChildren().clear();
        fridayItemContainer.getChildren().clear();
        saturdayItemContainer.getChildren().clear();
        sundayItemContainer.getChildren().clear();

        numberOfEntries = 0;
        for (Recipe recipe : recipeList){
            for (Map.Entry<Date, Integer> entry : recipe.getWeeklyDates().entrySet()){
                LocalDate localDate = entry.getKey().toLocalDate();
                WeekFields weekFields = WeekFields.of(Locale.getDefault());
                int weekOfYear = localDate.get(weekFields.weekOfWeekBasedYear());
                if(weekOfYear == week){
                    numberOfEntries ++;
                    DayOfWeek day = localDate.getDayOfWeek();
                    switch (day.name()) {
                        case "MONDAY":
                            displayWeeklyViewItem(localDate, recipe, mondayItemContainer, entry.getValue());
                            break;
                        case "TUESDAY":
                            displayWeeklyViewItem(localDate, recipe, tuesdayItemContainer, entry.getValue());
                            break;
                        case "WEDNESDAY":
                            displayWeeklyViewItem(localDate, recipe, wednesdayItemContainer, entry.getValue());
                            break;
                        case "THURSDAY":
                            displayWeeklyViewItem(localDate, recipe, thursdayItemContainer, entry.getValue());
                            break;
                        case "FRIDAY":
                            displayWeeklyViewItem(localDate, recipe, fridayItemContainer, entry.getValue());
                            break;
                        case "SATURDAY":
                            displayWeeklyViewItem(localDate, recipe, saturdayItemContainer, entry.getValue());
                            break;
                        case "SUNDAY":
                            displayWeeklyViewItem(localDate, recipe, sundayItemContainer, entry.getValue());
                            break;
                        default:
                            break;
                    }
                }
                
            }
        } 
    }

    public void updateView(){
        setRecipes(this.weeklyListTitle.getText(), week, recipeList);
        if (numberOfEntries == 0){
            System.out.println("updated and empty");
            recipeRepos.deleteShoppingList(week, user);
            this.weeklyListsSceneController.updateWeeklyLists();
        }
    }

    private void displayWeeklyViewItem(LocalDate date, Recipe recipe, VBox container, int servings) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/cookbook.view/WeeklyViewItem.fxml"));
            HBox recipeBox = fxmlLoader.load();
            WeeklyViewItemController controller = fxmlLoader.getController();
            controller.setText(recipe.getName());
            controller.setData(this.user, date, recipe);
            controller.setServings(servings);
            controllers.add(controller);
            container.getChildren().add(recipeBox);
            container.getProperties().put("Controller", this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
