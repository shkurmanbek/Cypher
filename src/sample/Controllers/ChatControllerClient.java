package sample.Controllers;
import java.io.*;
import java.net.URL;
import java.util.PriorityQueue;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.TreeMap;

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

import javax.swing.*;

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
    private Button button2;

    @FXML
    private Button button3;

    @FXML
    private Label login;

    @FXML
    private TextField chatField;

    @FXML
    private TextArea textArea;

    @FXML
    private Button vButton;

    @FXML
    private Button sButton;

    @FXML
    private Button dsButton;

    @FXML
    private Button dvButton;

    @FXML
    private TextField keyField;

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
    static PriorityQueue<Node> nodes = new PriorityQueue<>((o1, o2) -> (o1.value < o2.value) ? -1 : 1);
    static TreeMap<Character, String> codes = new TreeMap<>();
    static String text = "";
    static String encoded = "";
    static String decoded = "";
    static int ASCII[] = new int[128];
    class Node {
        Node left, right;
        double value;
        String character;

        public Node(double value, String character) {
            this.value = value;
            this.character = character;
            left = null;
            right = null;
        }

        public Node(Node left, Node right) {
            this.value = left.value + right.value;
            character = left.character + right.character;
            if (left.value < right.value) {
                this.right = right;
                this.left = left;
            } else {
                this.right = left;
                this.left = right;
            }
        }
    }
    static String generateKey(String str, String key)
    {
        int x = str.length();

        for (int i = 0; ; i++)
        {
            if (x == i)
                i = 0;
            if (key.length() == str.length())
                break;
            key+=(key.charAt(i));
        }
        return key;
    }

    // This function returns the encrypted text
// generated with the help of the key
    static String cipherText(String str, String key)
    {
        String cipher_text="";

        for (int i = 0; i < str.length(); i++) {
            // converting in range 0-25
            int x = (str.charAt(i) + key.charAt(i)) % 26;

            // convert into alphabets(ASCII)
            x += 'A';

            cipher_text += (char) (x);
        }
        return cipher_text;
    }

    // This function decrypts the encrypted text
// and returns the original text
    static String originalText(String cipher_text, String key)
    {
        String orig_text="";

        for (int i = 0 ; i < cipher_text.length() &&
                i < key.length(); i++)
        {
            // converting in range 0-25
            int x = (cipher_text.charAt(i) -
                    key.charAt(i) + 26) %26;

            // convert into alphabets(ASCII)
            x += 'A';
            orig_text+=(char)(x);
        }
        return orig_text;
    }
    public String deCeasarCipher(String str1, int shift) {
        int i, n;
        String str2="";
        char ch4;
        char ch2[]=str1.toCharArray();
        for(i=0;i<str1.length();i++)
        {
            if(Character.isLetter(ch2[i]))
            {
                if(((int)ch2[i]-shift)<97)
                {
                    ch4=(char)(((int)ch2[i]-shift-97+26)%26+97);

                }
                else
                {
                    ch4=(char)(((int)ch2[i]-shift-97)%26+97);
                }
                str2=str2+ch4;
            }

            else if(ch2[i]==' ')
            {
                str2=str2+ch2[i];
            }
        }
        return str2;
    }
    public String CeasarCipher(String str) {
        int shift,i,n;
        String str1="";
        String str2="";
        str=str.toLowerCase();
        n = str.length();
        char ch1[]=str.toCharArray();
        char ch3,ch4;
        shift=3;
        for(i=0;i<n;i++)
        {
            if(Character.isLetter(ch1[i]))
            {
                ch3=(char)(((int)ch1[i]+shift-97)%26+97);
                //System.out.println(ch1[i]+" = "+ch3);
                str1=str1+ch3;
            }
            else if(ch1[i]==' ')
            {
                str1=str1+ch1[i];
            }
        }
        return str1;
    }
    private void encodeText() {
        for (int i = 0; i < text.length(); i++)
            encoded += codes.get(text.charAt(i));
        chatField.setText(encoded);
    }

    private void buildTree(PriorityQueue<Node> vector) {
        while (vector.size() > 1)
            vector.add(new Node(vector.poll(), vector.poll()));
    }

    private void calculateCharIntervals(PriorityQueue<Node> vector, boolean printIntervals) {
        if (printIntervals) System.out.println("Probabilities: ");

        for (int i = 0; i < text.length(); i++)
            ASCII[text.charAt(i)]++;

        for (int i = 0; i < ASCII.length; i++)
            if (ASCII[i] > 0) {
                vector.add(new Node(ASCII[i] / (text.length() * 1.0), ((char) i) + ""));
                if (printIntervals)
                    System.out.println("'" + ((char) i) + "' : " + ASCII[i] / (text.length() * 1.0));
            }
    }

    private static void generateCodes(Node node, String s) {
        if (node != null) {
            if (node.right != null)
                generateCodes(node.right, s + "1");

            if (node.left != null)
                generateCodes(node.left, s + "0");

            if (node.left == null && node.right == null)
                codes.put(node.character.charAt(0), s);
        }
    }

    private void decodeText() {
        Node node = nodes.peek();
        for (int i = 0; i < encoded.length(); ) {
            Node tmpNode = node;
            while (tmpNode.left != null && tmpNode.right != null && i < encoded.length()) {
                if (encoded.charAt(i) == '1')
                    tmpNode = tmpNode.right;
                else tmpNode = tmpNode.left;
                i++;
            }
            if (tmpNode != null)
                if (tmpNode.character.length() == 1)
                    decoded += tmpNode.character;
                else
                    System.out.println("Input not Valid");

        }
        File file = new File("C:\\Users\\User\\Desktop\\decoded.txt");
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(decoded);
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                fr.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        chatField.setText(decoded);
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
            String message2 = chatField.getText();
            String message = finalUser.getUsername()+": ";
            message += message2;
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
        button2.setOnAction(event -> {
            String message = chatField.getText();
            message = deCeasarCipher(message, 3);
            chatField.setText(message);
        });
        button3.setOnAction(event -> {
            String message = chatField.getText();
            message = CeasarCipher(message);
            chatField.setText(message);
        });

        vButton.setOnAction(event -> {
            String message = chatField.getText();
            String keyword = keyField.getText();
            String key = generateKey(message, keyword);
            message = cipherText(message, key);
            chatField.setText(message);
        });

        dvButton.setOnAction(event -> {
            String message = chatField.getText();
            String keyword = keyField.getText();
            String key = generateKey(message, keyword);
            message = originalText(message, key);
            chatField.setText(message);
        });

        sButton.setOnAction(event -> {
            text = chatField.getText();
            calculateCharIntervals(nodes, true);
            buildTree(nodes);
            generateCodes(nodes.peek(), "");
            encodeText();
        });
        dsButton.setOnAction(event -> {
            decodeText();
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
