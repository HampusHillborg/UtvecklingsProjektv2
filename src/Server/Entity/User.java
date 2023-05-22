package Server.Entity;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class User implements Serializable {
    private final String imagePath;
    private final String username;
    private final int avatarHash;

    public User(String username, ImageIcon avatar) {
        // If avatar isn't jpg then compress
        this.username = username;
        avatarHash = avatar.hashCode();

        this.imagePath = "data/temp/" + avatarHash;
        if (!Files.exists(Paths.get(this.imagePath))) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.imagePath));
                oos.writeObject(avatar);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public ImageIcon getAvatar() {
        if (imagePath == null) return null;

        // Read the ImageIcon from the file
        ImageIcon imageFile = null;
        try {
            imageFile = (ImageIcon) new ObjectInputStream(new FileInputStream(imagePath)).readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return imageFile;
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

    public int getAvatarHash() {
        return avatarHash;
    }
}
