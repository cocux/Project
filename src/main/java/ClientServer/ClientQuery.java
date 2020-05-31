package ClientServer;

import com.google.gson.Gson;
import Data.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientQuery {


    private static Socket sock = null;
    private static Gson gson = new Gson();

    public ClientQuery() throws IOException {

    }

    public static Socket getSock() {
        return sock;
    }


    public static boolean createAccount(String userName, String pseudo, String password) throws IOException {
        String requestAccount = RequestCode.ACCOUNT+"*"+userName+"*"+pseudo+"*"+password;
        sock = new Socket("127.0.0.1",1515);
        BufferedOutputStream bos = new BufferedOutputStream(sock.getOutputStream());
        bos.write(requestAccount.getBytes());
        bos.flush();
        BufferedInputStream bis = new BufferedInputStream(sock.getInputStream());
        String response = "";
        int stream;
        byte[] b = new byte[4096];
        stream = bis.read(b);
        response = new String(b, 0, stream);
        return Boolean.parseBoolean(response);
    }

    public static User chatConnect(String userName, String password) throws IOException {
        String requestConnect = RequestCode.CHAT_CONNECT+"*"+userName+"*"+password;
        User user;
        sock = new Socket("127.0.0.1",1515);
        BufferedOutputStream bos1 = new BufferedOutputStream(sock.getOutputStream());
        bos1.write(requestConnect.getBytes());
        bos1.flush();
        BufferedInputStream bis = new BufferedInputStream(sock.getInputStream());
        String response = "";
        int stream;
        byte[] b = new byte[4096];
        stream = bis.read(b);
        response = new String(b, 0, stream);
        user = gson.fromJson(response, User.class);
        return user;
    }

    public static void chatDisconnect(String userName) throws IOException {
        String requestConnect = RequestCode.DECONNEXION+"*"+userName;
        BufferedOutputStream bos1 = new BufferedOutputStream(ClientQuery.getSock().getOutputStream());
        bos1.write(requestConnect.getBytes());
        bos1.flush();
        ClientQuery.getSock().close();

    }

    public static void addContact(String username, String contactUsermame) throws IOException {
        String requestConnect = RequestCode.ADD+"*"+username+"*"+contactUsermame;
        User user;

        BufferedOutputStream bos1 = new BufferedOutputStream(sock.getOutputStream());
        bos1.write(requestConnect.getBytes());
        bos1.flush();

    }
    public static void SendMsg(String username, String destinataire, String msg) throws IOException {
        String requestsMsg = RequestCode.SEND_MESSAGE+"*"+username+"*"+destinataire+"*"+msg;
        BufferedOutputStream bos1 = new BufferedOutputStream(sock.getOutputStream());
        bos1.write(requestsMsg.getBytes());
        bos1.flush();
    }

    public static void GetMsgHistory(String username, String destinataire) throws IOException {
        String requestsMsg = RequestCode.HISTORY+"*"+username+"*"+destinataire;
        BufferedOutputStream bos1 = new BufferedOutputStream(sock.getOutputStream());
        bos1.write(requestsMsg.getBytes());
        bos1.flush();
    }
}
