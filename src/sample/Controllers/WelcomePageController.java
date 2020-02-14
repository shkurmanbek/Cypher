package sample.Controllers;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.User;

public class WelcomePageController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label login;

    @FXML
    private Button testButton;

    @FXML
    private Button chatButton;

    @FXML
    private Button addInf;

    @FXML
    private Button infButton;

    @FXML
    private Button homeButton;

    @FXML
    void initialize() {
        User user = new User();
        try {
            ObjectInputStream inStream = new ObjectInputStream(new FileInputStream("save.data"));
            user = (User) inStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        login.setText(user.getUsername());

        chatButton.setOnAction(event -> {
            openNewScene("/sample/fxml/chat.fxml");
        });

        homeButton.setOnAction(event -> {
            openNewScene("/sample/fxml/sample.fxml");
        });

        infButton.setOnAction(event -> {
            openNewScene("/sample/fxml/app.fxml");
        });
    }
    public void openNewScene(String window){
        homeButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}

