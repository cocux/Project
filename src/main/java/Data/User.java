package Data;

import com.google.gson.annotations.SerializedName;
import java.util.*;


public class User {
    @SerializedName("name")
    private String name ;
    @SerializedName("login")
    private String login ;
    @SerializedName("password")
    private String password ;
    @SerializedName("Contacts")
    private ArrayList<Contact> Contacts = new ArrayList<Contact>();


    public User(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }



    public String getUserName() {
        return name;
    }

    public void setUserName(String name) {
        this.name = name;
    }

    public String getPseudo() {
        return login;
    }

    public void setPseudo(String pseudo) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Contact> getContacts() {
        return Contacts;
    }

    public void setContacts(Contact contact) {
        Contacts.add(contact);
    }

}

