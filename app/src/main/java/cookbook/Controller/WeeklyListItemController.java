package cookbook.Controller;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cookbook.DatabaseManager;
import cookbook.model.Recipe;
import cookbook.model.User;
import cookbook.repository.MySqlRecipeRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WeeklyListItemController {

    @FXML
    private Text dateOfWeek;

    @FXML
    private Text weekName;

    @FXML
    private VBox vBox;


    private MySqlRecipeRepository recipeRepos;
    private List<Recipe> weeklyRecipes;
    private User user;
    private String weeklyListTitle;

    @SuppressWarnings("deprecation")
    public void setWeeklyList(int year, int week, List<Recipe> weeklyrecipes) {
        this.weeklyRecipes = weeklyrecipes;

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        Date ms = calendar.getTime();
        LocalDate localDate = Instant.ofEpochMilli(ms.getTime())
                                     .atZone(ZoneId.systemDefault())
                                     .toLocalDate();

        this.weekName.setText(Integer.toString(week));
        this.dateOfWeek.setText(localDate + " - " + localDate.plusDays(6));
        weeklyListTitle = this.dateOfWeek.getText();
    }

    public void setUser(User user) {
        this.user = user;
        this.recipeRepos = new MySqlRecipeRepository(new DatabaseManager(), user);
    }

    @FXML
    void openWeeklyList(MouseEvent event) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookbook.view/WeeklyListScene.fxml"));

            // create new stage for new window of the recipe
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(fxmlLoader.load()));
            WeeklyListSceneController controller = fxmlLoader.getController();
            controller.setUser(user);
            controller.setRecipes(weeklyListTitle, Integer.parseInt(this.weekName.getText()), weeklyRecipes);
            controller.setListsController((WeeklyListsSceneController)this.vBox.getParent().getProperties().get("Controller"));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}