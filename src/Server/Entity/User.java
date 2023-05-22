package Server.Entity;

import javax.swing.*;
import java.io.Serializable;

public class User implements Serializable {
    private final String username;
    private final String imagePath;

    public User(String username, String imagePath) {
        this.username = username;
        this.imagePath = imagePath;
    }

    public String getUsername() {
        return username;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof User other)) return false;
        return this.username.equals(other.username);
    }
}
