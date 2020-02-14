package sample.Controllers;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.*;

public class ChatControllerClient extends Application {
    private boolean isServer = false;
    private NetworkConnection connection = isServer ? createServer() : createClient();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button button;

    @FXML
    private Button button1;

    @FXML
    private Label login;

    @FXML
    private TextField chatField;

    @FXML
    private TextArea textArea;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }
    @Override
    public void stop() throws Exception {
        connection.closeConnection();
    }
    @FXML
    void initialize() throws Exception {
        connection.startConnection();
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

        User finalUser = user;


        button.setOnAction(event -> {
            String message = finalUser.getUsername()+": ";
            message += chatField.getText();
            chatField.clear();

            textArea.appendText(message+"\n");

            try {
                connection.send(message);
            } catch (Exception e) {
                textArea.appendText("Failed to send!\n");
            }
        });
        button1.setOnAction(event -> {
            openNewScene("/sample/fxml/welcome.fxml");
        });
    }

    private Parent createContent() {

        VBox root = new VBox(20, textArea, chatField);
        root.setPrefSize(600, 600);
        return root;
    }


    Server createServer() {
        return new Server(55555, data -> {
            Platform.runLater(() -> {
                textArea.appendText(data.toString() + "\n");
            });
        });
    }

    Client createClient() {
        return new Client("127.0.0.1", 55555, data -> {
            Platform.runLater(() -> {
                textArea.appendText(data.toString() + "\n");
            });
        });
    }

    public void openNewScene(String window){
        button.getScene().getWindow().hide();
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
