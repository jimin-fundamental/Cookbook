package cookbook.Controller;

import java.io.IOException;
import java.util.List;

import cookbook.DatabaseManager;
import cookbook.model.Help;
import cookbook.model.Recipe;
import cookbook.repository.HelpDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class HelpViewSceneController {

    private List<Help> helpEntries;

    @FXML
    private VBox helpListVBox;

    @FXML
    private TextField searchTextField;

    @FXML
    public void searchClicked() {

    }

    private void retrieveHelpEntries() {
        DatabaseManager dbManager = new DatabaseManager();
        HelpDao helpDao = new HelpDao(dbManager);
        helpEntries = helpDao.getAllHelp();
    }

    private void displayHelpItem(Help helpEntry) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/cookbook.view/HelpItem.fxml"));
            VBox helpBox = fxmlLoader.load();
            HelpItemController controller = fxmlLoader.getController();
            controller.setHelpItem(helpEntry);
            helpListVBox.getChildren().add(helpBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize(){
        retrieveHelpEntries();
        for (Help help : helpEntries) {
            displayHelpItem(help);
        }
    }
}

