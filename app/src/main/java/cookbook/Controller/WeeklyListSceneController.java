package cookbook.Controller;

import java.io.IOException;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

import com.mysql.cj.protocol.x.SyncFlushDeflaterOutputStream;

import cookbook.model.Recipe;
import cookbook.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


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
    
    private List<Recipe> recipeList;
    private User user;
    private List<WeeklyViewItemController> controllers;

    public void setUser(User user){
        this.user = user;
    }

    @FXML
    private void deleteListClicked(ActionEvent event){
        for(WeeklyViewItemController controller : controllers){
            System.out.println("is removing...");
            controller.removeWeekly();
        }
    }

    public void setRecipes(String weeklyListTitle, int week, List<Recipe> recipeList) {
        this.controllers = new ArrayList<WeeklyViewItemController>();
        this.recipeList = recipeList;
        this.weeklyListTitle.setText(weeklyListTitle);

        for (Recipe recipe : recipeList){
            for (Date date : recipe.getWeeklyDates()){
                LocalDate localDate = date.toLocalDate();
                WeekFields weekFields = WeekFields.of(Locale.getDefault());
                int weekOfYear = localDate.get(weekFields.weekOfWeekBasedYear());
                if(weekOfYear == week){
                    DayOfWeek day = localDate.getDayOfWeek();
                    switch (day.name()) {
                        case "MONDAY":
                            displayWeeklyViewItem(localDate, recipe, mondayItemContainer);
                            break;
                        case "TUESDAY":
                            displayWeeklyViewItem(localDate, recipe, tuesdayItemContainer);
                            break;
                        case "WEDNESDAY":
                            displayWeeklyViewItem(localDate, recipe, wednesdayItemContainer);
                            break;
                        case "THURSDAY":
                            displayWeeklyViewItem(localDate, recipe, thursdayItemContainer);
                            break;
                        case "FRIDAY":
                            displayWeeklyViewItem(localDate, recipe, fridayItemContainer);
                            break;
                        case "SATURDAY":
                            displayWeeklyViewItem(localDate, recipe, saturdayItemContainer);
                            break;
                        case "SUNDAY":
                            displayWeeklyViewItem(localDate, recipe, sundayItemContainer);
                            break;
                        default:
                            break;
                    }
                }
                
            }
        } 
    }

    private void displayWeeklyViewItem(LocalDate date, Recipe recipe, VBox container) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/cookbook.view/WeeklyViewItem.fxml"));
            HBox recipeBox = fxmlLoader.load();
            WeeklyViewItemController controller = fxmlLoader.getController();
            controller.setText(recipe.getName());
            controller.setData(this.user, date, recipe);
            controllers.add(controller);
            container.getChildren().add(recipeBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
