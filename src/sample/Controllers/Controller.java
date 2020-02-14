package sample.Controllers;

import java.io.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DatabaseHandler;
import sample.User;
import sample.animations.Shake;

public class Controller {

        @FXML
        private ResourceBundle resources;

        @FXML
        private URL location;

        @FXML
        private TextField login_field;

        @FXML
        private TextField password_field;

        @FXML
        private Button signInButton;

        @FXML
        private Button loginSignUpButton;

        @FXML
        void initialize() {
            signInButton.setOnAction(event -> {

                String loginText = login_field.getText().trim();
                String loginPassword = password_field.getText().trim();

                if(!loginText.equals("") && !loginPassword.equals(""))
                    loginUser(loginText, loginPassword);
                if(loginText.equals("admin") && loginPassword.equals("123456"))
                    openNewScene("/sample/fxml/admin.fxml");
                else
                    System.out.println("Login and password is empty!");

            });

            loginSignUpButton.setOnAction(event -> {
                openNewScene("/sample/fxml/signup.fxml");
            });
        }

    private void loginUser(String loginText, String loginPassword) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = new User();
        user.setUsername(loginText);
        user.setPassword(loginPassword);
        ResultSet result = dbHandler.getUser(user);
        try {
            ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream("save.data"));
            outStream.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int counter = 0;

        while (true) {
            try {
                if (!result.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            counter++;
        }

        if (counter >= 1)
            openNewScene("/sample/fxml/welcome.fxml");
        else {
            Shake userLoginAnim = new Shake(login_field);
            Shake userPassAnim = new Shake(password_field);
            userLoginAnim.playAnim();
            userPassAnim.playAnim();
        }
    }
    public void openNewScene(String window){
        loginSignUpButton.getScene().getWindow().hide();
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

