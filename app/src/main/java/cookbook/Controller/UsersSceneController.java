package cookbook.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import cookbook.DatabaseManager;
import cookbook.model.User;
import cookbook.model.UserTable;
import cookbook.repository.MySqlRecipeRepository;
import cookbook.repository.UserDao;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

public class UsersSceneController {

    @FXML
    TableView<UserTable> usersTableView;

    @FXML
    public TableColumn<UserTable, Integer> nameColumn;

    @FXML
    public TableColumn<UserTable, String> usernameColumn;

    @FXML
    public TableColumn<UserTable, String> isAdminColumn;

    @FXML
    public TableColumn<UserTable, String> editColumn;

    private User user;

    private ObservableList<UserTable> userTable;

    public void setUserAndInitialize(User user) {
        this.user = user;

        DatabaseManager dbManager = new DatabaseManager();
        UserDao userDao = new UserDao(dbManager);
        List<User> users = userDao.getAllUser();
        users.removeIf(u -> u.getUserName().equals(user.getUserName()));

        userTable = FXCollections.observableArrayList(users.stream()
                .map(u -> new UserTable(u.getName(), u.getUserName(), u.getIsAdmin())).toArray(UserTable[]::new));

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("Username"));
        isAdminColumn.setCellValueFactory(new PropertyValueFactory<>("IsAdmin"));
        editColumn.setCellValueFactory(new PropertyValueFactory<>("Edit"));

        Callback<TableColumn<UserTable, String>, TableCell<UserTable, String>> cellFactory = //
                new Callback<TableColumn<UserTable, String>, TableCell<UserTable, String>>() {
                    @Override
                    public TableCell<UserTable, String> call(final TableColumn<UserTable, String> param) {
                        final TableCell<UserTable, String> cell = new TableCell<UserTable, String>() {

                            final Button btn = new Button("Edit");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        openEditUserScene(users.get(getIndex()));
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        editColumn.setCellFactory(cellFactory);

        usersTableView.setItems(userTable);
    }

    private void openEditUserScene(User user) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookbook.view/ModifyUserScene.fxml"));
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(fxmlLoader.load()));
            ModifyUserSceneController controller = fxmlLoader.getController();
            controller.setUser(user);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
