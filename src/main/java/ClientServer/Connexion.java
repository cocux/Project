package ClientServer;

import Data.*;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;


public class Connexion implements Runnable{

    private BufferedInputStream reader = null;
    private Socket connexion = null;
    private User user;
    private JTextArea textArea;
    private JComboBox contacts;
    private String reponse;
    
    public Connexion(Socket connexion, User user, JTextArea textArea, JComboBox contact){
        this.connexion = connexion;
        this.user = user;
        this.textArea = textArea;
        this.contacts = contact;
    }


    private String read() throws IOException {
        String response = "";
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        response = new String(b, 0, stream);
        return response;
    }

    public void run(){
        while(true) {
            try {
                reader = new BufferedInputStream(connexion.getInputStream());
                reponse = "";
                String response = read();
                Root Json = null;
                String json = "";
                String[] tabResponse = response.split(" ");
                RequestCode Code = RequestCode.values()[Integer.parseInt(tabResponse[0])-1];
                response = tabResponse[1];
                switch (Code) {
                    case DECONNEXION:
                        break;
                    case SEND_MESSAGE:
                        String expediteur = tabResponse[2];
                        if(expediteur.equals(contacts.getSelectedItem()))
                        {
                            reponse = response;
                            textArea.append(response);
                        }

                        break;
                    case CHANGE_PASSWORD:
                        reponse = response;
                        break;
                    case CHANGE_LOGIN:
                        reponse = response;
                        break;
                    case ADD:
                        reponse = response;
                        break;
                    case CREATE_GROUP:
                        break;
                    case HISTORY:
                        reponse = response;
                        break;
                }


            } catch (IOException e1) {
                e1.printStackTrace();
            }

            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getResponse(){
        return this.reponse;
    }
}
