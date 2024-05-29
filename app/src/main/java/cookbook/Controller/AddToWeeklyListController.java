package cookbook.Controller;

import java.awt.Color;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.util.ResourceBundle;

import cookbook.repository.ThemesRepository;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class AddToWeeklyListController implements Initializable{
    private Recipe recipe;

     @FXML
    private ToggleGroup weekday;
    
    @FXML
    private DatePicker weekDatePicker;

    @FXML
    private ComboBox<?> weeklyListComboBox;

    @FXML
    private Text recipeAddedText;
    
    private MySqlRecipeRepository recipeRepos;
    private User user;
    private int servings;

    public void setServings(int servings){
        this.servings = servings;
    }
    public void setRecipe(Recipe recipe) {
        
        this.recipe = recipe;
    }

    public void setUser(User user) {
    this.user = user;
    this.recipeRepos = new MySqlRecipeRepository(new DatabaseManager(), user);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recipeAddedText.setVisible(false);
        weekDatePicker.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                System.out.println("Scene is now set.");
                ThemesRepository.applyTheme(weekDatePicker.getScene());
            }
        });
    }

    @FXML
    void addToWeeklyList(ActionEvent event) {
        LocalDate localDate = weekDatePicker.getValue();
        Date date = Date.valueOf(localDate);
        recipeAddedText.setFill(javafx.scene.paint.Color.GREEN);
        recipeAddedText.setText("Recipe added!");
        recipeAddedText.setVisible(true);
        recipeRepos.addToWeekPlan(recipe, user, date, this.servings, recipeAddedText);
        

    }


    @FXML
    void datePickerClicked(MouseEvent event) {
        recipeAddedText.setVisible(false);

    }




}
