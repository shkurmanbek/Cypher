package sample.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button octButton;

    @FXML
    private Button civilWarButton;

    @FXML
    private Button WWButton;

    @FXML
    private Button ussrButton;

    @FXML
    private Button homeButton;

    @FXML
    void initialize() {
        homeButton.setOnAction(event -> {
            openNewScene("/sample/fxml/welcome.fxml");
        });
    }
    public void openNewScene(String window){
        homeButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));

        try {
            loader.load();
        } catch (IOException e){
            e.printStackTrace();
        }


        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}