package cookbook.Controller;

import java.io.IOException;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cookbook.DatabaseManager;
import cookbook.model.Recipe;
import cookbook.model.User;
import cookbook.repository.MySqlRecipeRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WeeklyListsSceneController {

    @FXML
    private VBox listsVBox;

    private List<Recipe> recipeList;
    private MySqlRecipeRepository recipeRepos;
    private User user;

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;

        // remove elements where weeklyDate is null
        Iterator<Recipe> iterator = recipeList.iterator();
        while (iterator.hasNext()) {
            Recipe recipe = iterator.next();
            if (recipe.getWeeklyDates() == null) {
                iterator.remove();
            }
        }


        Map<Integer, List<Recipe>> weeksMap = new HashMap<Integer, List<Recipe>>();
        for(Recipe recipe : recipeList){
            List<Date> dates = recipe.getWeeklyDates();
            for(Date date : dates){
                // get the week of which the date is in
                LocalDate localDate = date.toLocalDate();
                DayOfWeek day = localDate.getDayOfWeek();
                WeekFields weekFields = WeekFields.of(Locale.getDefault());
                int weekOfYear = localDate.get(weekFields.weekOfWeekBasedYear());
                List<Recipe> thatWeeksList = weeksMap.get(weekOfYear);
                    if(thatWeeksList == null){
                        thatWeeksList = new ArrayList<>();
                        weeksMap.put(weekOfYear, thatWeeksList);
                    }
                    if(!thatWeeksList.contains(recipe)){
                        thatWeeksList.add(recipe);
                    }
                    

            }
        }

        for (Map.Entry<Integer, List<Recipe>> entry : weeksMap.entrySet()) {
            int weekOfYear = entry.getKey();
            List<Recipe> recipesForWeek = entry.getValue();
            int year = recipesForWeek.get(0).getWeeklyDates().get(0).toLocalDate().getYear();
            
            // Call a method with the input List<Recipe>
            displayWeeklyListItem(year, weekOfYear, recipesForWeek);
        }

    }

    public void setUser(User user) {
        this.user = user;
        this.recipeRepos = new MySqlRecipeRepository(new DatabaseManager(), user);
    }

    @FXML
    void openWeeklyList() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookbook.view/WeeklyListScene.fxml"));

            // create new stage for new window of the recipe
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(fxmlLoader.load()));
            WeeklyListSceneController controller = fxmlLoader.getController();
            controller.setUser(user);
            controller.setRecipes(null, 0, recipeList);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addWeeklyList() {
    }

    private void displayWeeklyListItem(int year, int week, List<Recipe> weeklyRecipes) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/cookbook.view/WeeklyListItem.fxml"));
            VBox recipeBox = fxmlLoader.load();
            WeeklyListItemController controller = fxmlLoader.getController();
            controller.setUser(user);
            controller.setWeeklyList(year, week, weeklyRecipes);
            listsVBox.getChildren().add(recipeBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
