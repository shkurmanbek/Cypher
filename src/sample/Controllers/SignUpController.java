package sample.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DatabaseHandler;
import sample.User;

public class SignUpController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField signUpName;

    @FXML
    private TextField signUpSurname;

    @FXML
    private TextField signUpCountry;

    @FXML
    private CheckBox signUpCheckboxMale;

    @FXML
    private CheckBox signUpCheckboxFemale;

    @FXML
    private TextField signUpLogin;

    @FXML
    private TextField signUpPassword;

    @FXML
    private Button signUpButton;

    @FXML
    private Button backButton;

    @FXML
    void initialize() {

        signUpButton.setOnAction(event -> {
            String loginText = signUpLogin.getText().trim();
            String loginPassword = signUpPassword.getText().trim();
            String loginName = signUpName.getText().trim();
            String loginSurname = signUpSurname.getText().trim();
            String loginCountry = signUpCountry.getText().trim();
            if(!loginText.equals("") && !loginPassword.equals("")&& !loginName.equals("")&& !loginSurname.equals("")&& !loginCountry.equals("")){
                signUpNewUser();
                openNewScene("/sample/fxml/welcome.fxml");
            }
            else
                System.out.println("Login and password is empty!");

        });

        backButton.setOnAction(event -> {
            openNewScene("/sample/fxml/sample.fxml");
        });

    }

    private void signUpNewUser() {
        DatabaseHandler dbHandler =new DatabaseHandler();

        String firstname = signUpName.getText();
        String lastname = signUpSurname.getText();
        String username = signUpLogin.getText();
        String password = signUpPassword.getText();
        String location = signUpCountry.getText();
        String gender = "";
        if(signUpCheckboxMale.isSelected())
            gender = "Male";
        else
            gender = "Female";
        User user = new User(firstname, lastname, username, password, location, gender);

        dbHandler.signUpUser(user);
    }
    public void openNewScene(String window){
        signUpButton.getScene().getWindow().hide();
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
