package cookbook.Controller;

import java.io.IOException;
import java.util.List;

import cookbook.DatabaseManager;
import cookbook.model.Help;
import cookbook.model.Recipe;
import cookbook.repository.HelpDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;


public class HelpViewSceneController {

    private List<Help> helpEntries;

    @FXML
    private VBox helpListVBox;

    @FXML
    private TextField searchTextField;

    @FXML
    public void searchClicked() {
        String[] searchWords = searchTextField.getText().split(", ");
        // clear all displayed elements
        helpListVBox.getChildren().clear();
        // iterate through all recipes
        for (Help help : helpEntries) {
            // Title of each entry -> search in these
            String title = help.getTitle();
            String text = help.getText();
            
            String searchHits = "";
            int numberOfHits = 0;
            for (String searchWord : searchWords){
                boolean hit = false;
                // check if the search word is in the helptitle
                if (title.toLowerCase().contains(searchWord.toLowerCase())) {
                    hit = true;
                    numberOfHits += 1;
                }
                // check if the search word is in the helptext
                if (text.toLowerCase().contains(searchWord.toLowerCase())) {
                    hit = true;
                    numberOfHits += 1;
                }
                
                // for (String tag : recipe.getTags()) {
                //     if (tag.toLowerCase().contains(searchWord.toLowerCase())) {
                //         if (hit != true)
                //             numberOfHits += 1;
                //         hit = true;
                //         if (!searchWord.isEmpty() && !searchHits.contains(tag)) {
                //             searchHits += tag + ", ";
                //         }
                //     }
                // }

            }
            // add the recipe if found
            if (numberOfHits >= searchWords.length) {
                // cut the last ", "
                // if (searchHits.length() >= 3)
                //     searchHits = searchHits.substring(0, searchHits.length() - 2);
                // show selected Item
                displayHelpItem(help);
            }
        }
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

    public void initializeHelp(){
        retrieveHelpEntries();
        for (Help help : helpEntries) {
            displayHelpItem(help);
        }
    }
}

