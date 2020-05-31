package Data;


import java.util.*;


public class Root {

    ArrayList<User> user = new ArrayList<User>();

    public Root(ArrayList<User> user) {
        this.user = user;
    }

    public ArrayList<User> getUser() {
        return this.user;
    }

    public void setUtilisateur(User user) {
        this.user.add(user);
    }
}
