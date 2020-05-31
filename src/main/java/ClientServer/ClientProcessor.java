package ClientServer;
import Data.*;
import com.google.gson.Gson;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Hashtable;

public class ClientProcessor implements Runnable{

    private Socket sock;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;
    private Hashtable dic;
    private User currentUser = null;
    private Socket socketClient;
    public ClientProcessor(Socket pSock, Hashtable dic){
        sock = pSock;
        socketClient = pSock;
        this.dic = dic;
    }

    private String read() throws IOException{
        String response = "";
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        response = new String(b, 0, stream);
        return response;
    }

    @Override
    public void run() {
        System.err.println("Lancement du traitement de la connexion cliente");

        boolean closeConnexion = false;
        while(!sock.isClosed()){

            try {
                writer = new PrintWriter(sock.getOutputStream());
                reader = new BufferedInputStream(sock.getInputStream());
                String response = read();
                InetSocketAddress remote = (InetSocketAddress)sock.getRemoteSocketAddress();
                String debug = "";

                debug = "Thread : " + Thread.currentThread().getName() + ". ";
                debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
                debug += " Sur le port : " + remote.getPort() + ".\n";
                debug += "\t -> Commande reçue : " + response + "\n";
                System.err.println("\n" + debug);

                String toSend = "";
                String[] tabResponse = response.split("||");
                RequestCode Code = RequestCode.values()[Integer.parseInt(tabResponse[0])-1];
                Root Json = null;
                String json = "";
                try {
                    switch (Code) {
                        case ACCOUNT:
                            Json = Data.SerializationMessage.Deserialization("Json.json");
                            for (User base : Json.getUser()) {
                                if (base.getUserName().equals(tabResponse[1]) ||
                                        base.getPseudo().equals(tabResponse[2])) {
                                    toSend = "false";
                                }

                            }
                            if (!toSend.equals("false")) {
                                User nouveau = new User(tabResponse[1], tabResponse[2], tabResponse[3]);
                                Json.setUtilisateur(nouveau);
                                SerializationMessage.Serialization(Json, "Json.json");
                                toSend = "true";
                            }
                            break;
                        case CHAT_CONNECT:
                            Json = SerializationMessage.Deserialization("Json.json");
                            json = "";
                            for (User base : Json.getUser()) {
                                if (base.getUserName().equals(tabResponse[1]) &&
                                        base.getPassword().equals(tabResponse[2]))
                                {
                                    currentUser = base;
                                    Gson gson = new Gson();
                                    json = gson.toJson(base);
                                    if(!dic.containsKey(base.getUserName()))
                                    {
                                        dic.put(base.getUserName(),sock);
                                    }
                                }
                            }
                            if (json.equals("")) {
                                toSend = "null";
                            } else {
                                toSend = json;
                            }
                            break;
                        case DECONNEXION:

                            Json = SerializationMessage.Deserialization("Json.json");
                            json = "";
                            for (User base : Json.getUser()) {
                                if (base.getUserName().equals(tabResponse[1]))
                                {
                                    if(dic.containsKey(base.getUserName()))
                                    {
                                        dic.remove(base.getUserName());
                                    }
                                }
                            }
                            break;
                        case SEND_MESSAGE:
                            String user = tabResponse[1];
                            String destinataire = tabResponse[2];
                            String msg = tabResponse[3];

                            Json = SerializationMessage.Deserialization("Json.json");
                            for (User base : Json.getUser()) {
                                if(base.getUserName().equals(user)) {
                                    for (Contact contacts : base.getContacts()) {
                                        if (contacts.getLogin().equals(destinataire)) {
                                            Message message = new Message(msg,destinataire,base.getPseudo());
                                            contacts.setMessage(message);

                                            sock = (Socket) dic.get(contacts.getName());
                                            writer = new PrintWriter(sock.getOutputStream());
                                            toSend = RequestCode.SEND_MESSAGE + "*"+msg+"*"+currentUser.getPseudo();
                                        }
                                    }
                                }
                            }
                            for (User base : Json.getUser()) {
                                if (base.getPseudo().equals(destinataire)) {
                                    for (Contact contacts : base.getContacts()) {
                                        if (contacts.getLogin().equals(user)) {
                                            Message message = new Message(msg, destinataire, base.getPseudo());
                                            contacts.setMessage(message);
                                        }
                                    }
                                }
                            }

                            SerializationMessage.Serialization(Json,"Json.json");




                            break;
                        case CHANGE_PASSWORD:
                            Json = SerializationMessage.Deserialization("Json.json");
                            for (User base : Json.getUser()) {
                                if(base.getUserName().equals(tabResponse[1]) &&
                                        base.getPassword().equals(tabResponse[2]))
                                {
                                    base.setPassword(tabResponse[3]);
                                    SerializationMessage.Serialization(Json,"Json.json");
                                    toSend=RequestCode.CHANGE_PASSWORD+"*true";

                                }
                                if (!toSend.equals(RequestCode.CHANGE_PASSWORD+"*true")){
                                    toSend= RequestCode.CHANGE_PASSWORD+"*false";
                                }
                            }
                            break;
                        case CHANGE_LOGIN:
                            Json = SerializationMessage.Deserialization("Json.json");
                            json = "";
                            for (User base : Json.getUser()) {
                                if (base.getUserName().equals(tabResponse[1])) {
                                    base.setPseudo(tabResponse[2]);
                                    SerializationMessage.Serialization(Json, "Json.json");
                                    toSend= RequestCode.CHANGE_LOGIN+"*true";

                                }
                                if (!toSend.equals(RequestCode.CHANGE_LOGIN+"*true")){
                                    toSend= RequestCode.CHANGE_LOGIN+"*false";
                                }
                            }


                            break;
                        case ADD:
                            Json = SerializationMessage.Deserialization("Json.json");
                            json = "";
                            Contact contacts = null;
                            for (User base : Json.getUser()) {
                                if(base.getPseudo().equals(tabResponse[2])) {
                                    contacts = new Contact(base.getPseudo(), base.getUserName());
                                    Gson gson = new Gson();
                                    json = RequestCode.ADD+"*"+gson.toJson(contacts);
                                }
                            }

                            if(json.equals("")) {
                                toSend = RequestCode.ADD+"*false";
                            } else {
                                for (User base : Json.getUser()) {
                                    if(base.getUserName().equals(tabResponse[1])) {
                                        base.getContacts().add(contacts);
                                        SerializationMessage.Serialization(Json, "Json.json");
                                    }
                                }
                                toSend = json;
                            }
                            break;
                        case CREATE_GROUP:
                            break;
                        case HISTORY:
                            Json = SerializationMessage.Deserialization("Json.json");
                            json = "";
                            String username = tabResponse[1];
                            String contact = tabResponse[2];
                            for(User base : Json.getUser())
                            {
                                if(base.getUserName().equals(username))
                                {
                                    for (Contact contactHistory : base.getContacts())
                                    {
                                        if(contactHistory.getLogin().equals(contact))
                                        {
                                            Gson gson = new Gson();

                                            json = gson.toJson(contactHistory.getMessage());
                                        }

                                    }
                                }
                            }
                            if(json.equals(""))
                            {
                                toSend = "null";
                            }
                            else
                            {
                                toSend = RequestCode.HISTORY+"*"+json;
                            }
                            break;
                    }} catch (IOException e) {
                    e.printStackTrace();
                }
                writer.write(toSend);
                writer.flush();
                sock = socketClient;
            }catch(SocketException e){
                System.err.println("Connexion Stopped");
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
