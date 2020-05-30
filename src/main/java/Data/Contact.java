package Data;

import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;

import java.util.*;

public class Contact{
    @SerializedName("login")
    private String login="";
    @SerializedName("name")
    private String name="";
    @SerializedName("Message")
    private ArrayList<Message> Message = new ArrayList<Message>();

    public Contact(String login, String name) {
        this.login = login;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public ArrayList<Message> getMessage() {
        return Message;
    }

    public void setMessage(Message message) {
        Message.add(message);
    }
}