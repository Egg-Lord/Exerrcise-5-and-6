import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class HomeController implements Initializable {

    ObservableList<User> userlist = FXCollections.observableArrayList();

    @FXML
    private Label homelabel;
    @FXML
    private Button btncreate;
    @FXML
    private Button btndelete;
    @FXML
    private Button btnupdate;
    @FXML
    private TextField passwordtextfield;
    @FXML
    private TextField nametextfield;
    @FXML
    private TableView<User> mytable;
    @FXML
    private TableColumn<User, String> passwordcol;
    @FXML
    private TableColumn<User, String> usernamecol;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        homelabel.setText("Welcome");
        initializeCol();
        displayUsers();
    }

    public void displayName(String uname) {
        homelabel.setText("Welcome " + uname + "!");
    }

    private void initializeCol() {
        usernamecol.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordcol.setCellValueFactory(new PropertyValueFactory<>("password"));
    }

    private void displayUsers() {
        userlist.clear();
        ResultSet rs = DatabaseHandler.getInstance().execQuery("SELECT * FROM login");
        try {
            while (rs.next()) {
                userlist.add(new User(rs.getString("username"), rs.getString("userpassword")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mytable.setItems(userlist);
    }

    @FXML
    private void createUser(ActionEvent event) {
        String username = nametextfield.getText();
        String pword = passwordtextfield.getText();

        username = username.trim();
        pword = pword.trim();

        if (username.length() == 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Empty username");
            alert.showAndWait();
            return;
        }

        if (pword.length() == 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Empty password");
            alert.showAndWait();
            return;
        }

        User user = new User(username, pword);
        if (DatabaseHandler.adduser(user)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("User Created");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Failed to create user");
            alert.showAndWait();
        }

        displayUsers();
    }

    @FXML
    private void deleteUser(ActionEvent event) {
        User user = mytable.getSelectionModel().getSelectedItem();

        if (user == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("No user selected");
            alert.showAndWait();
            return;
        }

        if (DatabaseHandler.deleteUser(user)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("User Deleted");
            alert.showAndWait();
            displayUsers();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Failed to delete user");
            alert.showAndWait();
        }
    }

    @FXML
    private void updateUser(ActionEvent event) {
        User user = mytable.getSelectionModel().getSelectedItem();

        if (user == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("No user selected");
            alert.showAndWait();
            return;
        }

        String newUsername = nametextfield.getText().trim();
        String newPassword = passwordtextfield.getText().trim();

        if (newUsername.length() == 0 || newPassword.length() == 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Username or password cannot be empty");
            alert.showAndWait();
            return;
        }

        user.setUsername(newUsername);
        user.setPassword(newPassword);

        if (DatabaseHandler.updateUser(user)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setContentText("User Updated");
            alert.showAndWait();
            displayUsers();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Failed to update user");
            alert.showAndWait();
        }
    }
}