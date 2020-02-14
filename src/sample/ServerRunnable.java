package sample;
import java.io.*;
import java.net.*;
public class ServerRunnable extends Thread{
    private Socket socket;

    public ServerRunnable(){

    }
    public ServerRunnable(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
