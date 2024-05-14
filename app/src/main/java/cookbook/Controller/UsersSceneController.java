package cookbook.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import cookbook.DatabaseManager;
import cookbook.model.User;
import cookbook.model.UserTable;
import cookbook.repository.MySqlRecipeRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class UsersSceneController {

    @FXML
    TableView usersTableView;

    @FXML
    public TableColumn<UserTable, Integer> nameColumn;

    @FXML
    public TableColumn<UserTable, String> usernameColumn;

    @FXML
    public TableColumn<UserTable, String> isAdminColumn;

    @FXML
    public TableColumn<UserTable, String> editColumn;

    private User user;

    public void setUserAndInitialize(User user) {
        this.user = user;

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("Username"));
        isAdminColumn.setCellValueFactory(new PropertyValueFactory<>("IsAdmin"));
        editColumn.setCellValueFactory(new PropertyValueFactory<>("Edit"));
    }

    @FXML
    public void addUserClicked(ActionEvent event) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookbook.view/AddUserScene.fxml"));

            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
