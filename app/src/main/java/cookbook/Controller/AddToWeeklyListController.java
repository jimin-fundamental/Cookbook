package cookbook.Controller;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.util.ResourceBundle;

import cookbook.DatabaseManager;
import cookbook.model.Recipe;
import cookbook.model.User;
import cookbook.repository.MySqlRecipeRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ToggleGroup;

public class AddToWeeklyListController implements Initializable{
    private Recipe recipe;

     @FXML
    private ToggleGroup weekday;
    
    @FXML
    private DatePicker weekDatePicker;

    @FXML
    private ComboBox<?> weeklyListComboBox;
    
    private MySqlRecipeRepository recipeRepos;
    private User user;

    
    public void setRecipe(Recipe recipe) {
        
        this.recipe = recipe;
    }
        public void setUser(User user) {
        this.user = user;
        this.recipeRepos = new MySqlRecipeRepository(new DatabaseManager(), user);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    
    }

    @FXML
    void addToWeeklyList(ActionEvent event) {
        LocalDate localDate = weekDatePicker.getValue();
        Date date = Date.valueOf(localDate);
        recipeRepos.addToWeekPlan(recipe, user, date);
        System.out.println("recipe added");

    }




}
