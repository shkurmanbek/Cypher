package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeMap;

class Hamming {
    String syndrome;
    String block_code;
    String character;
    public Hamming(String syndrome, String block_code){
        this.syndrome = syndrome;
        this.block_code = block_code;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

}

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
public class ShannonFanoController {

    @FXML
    private TextField textField;

    @FXML
    private Button compress;

    @FXML
    private TextField compressedText;

    @FXML
    private TextArea redudantText;

    @FXML
    private TextArea errorText;

    @FXML
    private TextArea detectedText;

    @FXML
    private TextArea correctedText;

    @FXML
    private Button addBits;

    @FXML
    private Button errorButton;

    @FXML
    private Button correctError;

    @FXML
    private Button detectButton;

    static PriorityQueue<Node> nodes = new PriorityQueue<>((o1, o2) -> (o1.value < o2.value) ? -1 : 1);
    static TreeMap<Character, String> codes = new TreeMap<>();
    static String text = "";
    static String encoded = "";
    static String decoded = "";
    static int count = 0;
    static int ASCII[] = new int[128];
    static Hamming err[] = new Hamming[128];
    private static String readFromText(String scanner){
        text = scanner;
        ASCII = new int[128];
        nodes.clear();
        codes.clear();
        encoded = "";
        decoded = "";
        System.out.println("Text:\n " + text);
        calculateCharIntervals(nodes, true);
        buildTree(nodes);
        generateCodes(nodes.peek(), "");
        for (int i = 0; i < text.length(); i++)
            encoded += codes.get(text.charAt(i));
        count = encoded.length();
        return encoded;
    }
    private static String addBits(String compressed){
        int j = 4;
        String error_cor = "";
        for(int i = 0; i<(compressed.length()); i++){
            if(j<=compressed.length()){
                String s1 = compressed.substring(i, j) + encode_block(compressed.substring(i, j));
                error_cor += s1;
                j++;
            }
        }
        return error_cor;
    }
    private static String createErrors(String redudant){
        String errored = "";
        char[] myNameChars = redudant.toCharArray();
        int k=1;
        int p=2;
        for(int i=0; i<encoded.length()-3; i++){
            if (myNameChars[k]=='1')
                myNameChars[k]= '0';
            else if (myNameChars[p]=='0')
                myNameChars[p]= '1';
            else
                myNameChars[k]= myNameChars[k];
            k+=7;
            p+=7;
        }
        errored = String.valueOf(myNameChars);
        return errored;
    }

    private static String detectErrors(String errored){
        String detected = "";
                int l = 7;
        int c = 0;
        String s1_block = "";
        for(int i = 0; i<(errored.length()); i+=7){
            if(l<errored.length()+7){
                s1_block = errored.substring(i, l);
                detected += "Block code: "+s1_block+", Syndrome: "+block_error_syndrome(s1_block)+"\n";
                l+=7;
                Hamming error = new Hamming(block_error_syndrome(s1_block), s1_block);
                err[c] = error;
                c++;
            }
        }
        return detected;
    }

    private static String correctErrors(String detected) {
        String edited_block = "";

        for (int i = 0; i < count-3; i++) {
            if (err[i].syndrome.equals("000")) {
                edited_block += err[i].block_code;
                System.out.println("Corrected block code: " + err[i].block_code);
            }
            if (err[i].syndrome.equals("001")) {
                char[] block_code_chars = err[i].block_code.toCharArray();
                if (block_code_chars[6] == '0')
                    block_code_chars[6] = '1';
                else if (block_code_chars[6] == '1')
                    block_code_chars[6] = '0';
                System.out.println("Edited Block: " + String.valueOf(block_code_chars));
                edited_block += String.valueOf(block_code_chars);
            }
            if (err[i].syndrome.equals("010")) {
                char[] block_code_chars = err[i].block_code.toCharArray();
                if (block_code_chars[5] == '0')
                    block_code_chars[5] = '1';
                else if (block_code_chars[5] == '1')
                    block_code_chars[5] = '0';
                System.out.println("Corrected block code: " + String.valueOf(block_code_chars));
                edited_block += String.valueOf(block_code_chars);
            }
            if (err[i].syndrome.equals("011")) {
                char[] block_code_chars = err[i].block_code.toCharArray();
                if (block_code_chars[3] == '0')
                    block_code_chars[3] = '1';
                else if (block_code_chars[3] == '1')
                    block_code_chars[3] = '0';
                System.out.println("Corrected block code: " + String.valueOf(block_code_chars));
                edited_block += String.valueOf(block_code_chars);
            }
            if (err[i].syndrome.equals("100")) {
                char[] block_code_chars = err[i].block_code.toCharArray();
                if (block_code_chars[4] == '0')
                    block_code_chars[4] = '1';
                else if (block_code_chars[4] == '1')
                    block_code_chars[4] = '0';
                System.out.println("Corrected block code: " + String.valueOf(block_code_chars));
                edited_block += String.valueOf(block_code_chars);
            }
            if (err[i].syndrome.equals("101")) {
                char[] block_code_chars = err[i].block_code.toCharArray();
                if (block_code_chars[0] == '0')
                    block_code_chars[0] = '1';
                else if (block_code_chars[0] == '1')
                    block_code_chars[0] = '0';
                System.out.println("Corrected block code: " + String.valueOf(block_code_chars));
                edited_block += String.valueOf(block_code_chars);
            }
            if (err[i].syndrome.equals("110")) {
                char[] block_code_chars = err[i].block_code.toCharArray();
                if (block_code_chars[2] == '0')
                    block_code_chars[2] = '1';
                else if (block_code_chars[2] == '1')
                    block_code_chars[2] = '0';
                System.out.println("Corrected block code: " + String.valueOf(block_code_chars));
                edited_block += String.valueOf(block_code_chars);
            }
            if (err[i].syndrome.equals("111")) {
                char[] block_code_chars = err[i].block_code.toCharArray();
                if (block_code_chars[1] == '0')
                    block_code_chars[1] = '1';
                else if (block_code_chars[1] == '1')
                    block_code_chars[1] = '0';

                System.out.println("Corrected block code: " + String.valueOf(block_code_chars));
                edited_block += String.valueOf(block_code_chars);
            }
        }
        return edited_block;
    }


    private static String decodeText(String corrected) {
        decoded = "";
        Node node = nodes.peek();
        for (int i = 0; i < corrected.length(); i++) {
            Node tmpNode = node;
            while (tmpNode.left != null && tmpNode.right != null && i < encoded.length()) {
                if (corrected.charAt(i) == '1')
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
        return decoded;
    }
    private static String encode_block(String encStr){
        int r1 = (Character.getNumericValue(encStr.charAt(0))+Character.getNumericValue(encStr.charAt(1))+Character.getNumericValue(encStr.charAt(2)))%2;
        int r2 = (Character.getNumericValue(encStr.charAt(1))+Character.getNumericValue(encStr.charAt(2))+Character.getNumericValue(encStr.charAt(3)))%2;
        int r3 = (Character.getNumericValue(encStr.charAt(0))+Character.getNumericValue(encStr.charAt(1))+Character.getNumericValue(encStr.charAt(3)))%2;
        String rs1 = String.valueOf(r1);
        String rs2 = String.valueOf(r2);
        String rs3 = String.valueOf(r3);
        String s = rs1+rs2+rs3;
        return s;
    }

    private static String block_error_syndrome(String encStr){
        int r1 = (Character.getNumericValue(encStr.charAt(0))+Character.getNumericValue(encStr.charAt(1))+Character.getNumericValue(encStr.charAt(2))+Character.getNumericValue(encStr.charAt(4)))%2;
        int r2 = (Character.getNumericValue(encStr.charAt(1))+Character.getNumericValue(encStr.charAt(2))+Character.getNumericValue(encStr.charAt(3))+Character.getNumericValue(encStr.charAt(5)))%2;
        int r3 = (Character.getNumericValue(encStr.charAt(0))+Character.getNumericValue(encStr.charAt(1))+Character.getNumericValue(encStr.charAt(3))+Character.getNumericValue(encStr.charAt(6)))%2;
        String rs1 = String.valueOf(r1);
        String rs2 = String.valueOf(r2);
        String rs3 = String.valueOf(r3);
        String s = rs1+rs2+rs3;
        return s;
    }


    @FXML
    private Button homeButton;
    private static void buildTree(PriorityQueue<Node> vector) {
        while (vector.size() > 1)
            vector.add(new Node(vector.poll(), vector.poll()));
    }

    private static void calculateCharIntervals(PriorityQueue<Node> vector, boolean printIntervals) {
        if (printIntervals) System.out.println("-- intervals --");

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
    @FXML
    void initialize() {
        compress.setOnAction(event -> {
            String compressed = "";
            compressed = textField.getText();
            text = readFromText(compressed);
            compressedText.setText(text);
        });

        addBits.setOnAction(event -> {
            String redudant = addBits(compressedText.getText());
            redudantText.setText(redudant);
        });

        errorButton.setOnAction(event -> {
            String errors = createErrors(redudantText.getText());
            errorText.setText(errors);
        });

        detectButton.setOnAction(event -> {
            String detected = detectErrors(errorText.getText());
            detectedText.setText(detected);
        });

        correctError.setOnAction(event -> {
            String corrected = correctErrors(detectedText.getText());
            correctedText.setText(corrected);
        });


        homeButton.setOnAction(event -> {
            openNewScene("/sample/fxml/sample.fxml");
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
