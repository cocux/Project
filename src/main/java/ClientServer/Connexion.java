package ClientServer;

import Data.*;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;


public class Connexion implements Runnable{

    private Socket connexion = null;
    private BufferedInputStream reader = null;
    private User user;
    private JTextArea textArea;
    private JComboBox contact;
    
    public Connexion(Socket connexion, User user, JTextArea textArea, JComboBox contact){
        this.connexion = connexion;
        this.user = user;
        this.textArea = textArea;
        this.contact = contact;
    }


    private String read() throws IOException {
        String response = "";
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        response = new String(b, 0, stream);
        return response;
    }

    @Override
    public void run() {

    }
}
