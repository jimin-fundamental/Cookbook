package cookbook.Controller;

import java.io.IOException;
import java.net.URL;
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
import java.util.ResourceBundle;

import cookbook.DatabaseManager;
import cookbook.model.Recipe;
import cookbook.model.User;
import cookbook.repository.MySqlRecipeRepository;
import cookbook.repository.ThemesRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WeeklyListsSceneController implements Initializable{

    @FXML
    private VBox listsVBox;

    private List<Recipe> recipeList;
    private MySqlRecipeRepository recipeRepos;
    private User user;
    private int numberOfElements;

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
        numberOfElements = 0;
        Map<Integer, List<Recipe>> weeksMap = new HashMap<Integer, List<Recipe>>();
        for(Recipe recipe : recipeList){
            if(recipe.getWeeklyDates() != null){
                Map<Date, Integer> dates = recipe.getWeeklyDates();
                for(Map.Entry<Date, Integer> entry : dates.entrySet()){
                    // get the week of which the date is in
                    LocalDate localDate = entry.getKey().toLocalDate();
                    WeekFields weekFields = WeekFields.of(Locale.getDefault());
                    int weekOfYear = localDate.get(weekFields.weekOfWeekBasedYear());
                    List<Recipe> thatWeeksList = weeksMap.get(weekOfYear);
                    if(thatWeeksList == null){
                        numberOfElements++;
                        thatWeeksList = new ArrayList<>();
                        weeksMap.put(weekOfYear, thatWeeksList);
                    }
                    if(!thatWeeksList.contains(recipe)){
                        thatWeeksList.add(recipe);
                    }
                        
    
                }

            }
        }
        if(!listsVBox.getProperties().containsKey("Controller")){
            listsVBox.getProperties().put("Controller", this);
        }
        listsVBox.getChildren().clear();
        for (Map.Entry<Integer, List<Recipe>> entry : weeksMap.entrySet()) {
            int weekOfYear = entry.getKey();
            List<Recipe> recipesForWeek = entry.getValue();
            List<Date> list = new ArrayList<>(recipesForWeek.get(0).getWeeklyDates().keySet());
            int year = list.get(0).toLocalDate().getYear();
            
            // Call a method with the input List<Recipe>
            displayWeeklyListItem(year, weekOfYear, recipesForWeek);
        }

    }

    public void updateWeeklyLists(){
        System.out.println("updating...");
        setRecipeList(this.recipeList);
        if(this.numberOfElements == 0){
            ((Stage)listsVBox.getScene().getWindow()).close();
        }
    }

    public void setUser(User user) {
        this.user = user;
        this.recipeRepos = new MySqlRecipeRepository(new DatabaseManager(), user);
    }

    @FXML
    void openWeeklyList() {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listsVBox.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                System.out.println("Scene is now set.");
                ThemesRepository.applyTheme(listsVBox.getScene());
            }
        });
    }

}
