package Client.Entity;

import java.io.Serializable;

public class Contact implements Serializable {
    private String username;
    private String imagePath;

    public Contact(String username, String imagePath) {
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
}
