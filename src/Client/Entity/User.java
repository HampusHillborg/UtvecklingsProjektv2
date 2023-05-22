package Client.Entity;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String username;
    private Icon icon;
    private ArrayList<String> contacts;

    public User(String username, Icon icon) {
        this.username = username;
        this.icon = null;
        this.contacts = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public Icon getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return String.format("User: " + username);
    }
}
