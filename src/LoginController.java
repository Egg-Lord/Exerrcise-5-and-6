import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

public class LoginController {

    @FXML
    private Label labelUsername;
    @FXML
    private Label labelPassword;
    @FXML
    private TextField textUsername;
    @FXML
    private TextField textPassword;
    @FXML
    private Button loginbutton;

        private Stage stage;
        private Scene scene;
        private Parent root;

    @FXML
        public void loginbuttonHandler(ActionEvent event) throws IOException {
        String uname = textUsername.getText();
        String pword = textPassword.getText();

        if (DatabaseHandler.validateLogin(uname, pword)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
            root = loader.load();

            HomeController homeController = loader.getController();
            homeController.displayName(uname);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else 
        {
            System.err.println("Invalid username or password");
        }
    }
}