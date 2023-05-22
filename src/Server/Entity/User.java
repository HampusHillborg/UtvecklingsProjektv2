package Server.Entity;

import javax.swing.*;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class User implements Serializable {
    private final String username;
    private final byte[] avatarBytes;

    public User(String username, String imagePath) {
        this.username = username;
        this.avatarBytes = loadImageBytes(imagePath);
    }

    public String getUsername() {
        return username;
    }

    public ImageIcon getAvatar() {
        if (avatarBytes == null) return null;

        // Convert the byte array back to ImageIcon
        return new ImageIcon(avatarBytes);
    }

    private byte[] loadImageBytes(String imagePath) {
        try {
            return Files.readAllBytes(Paths.get(imagePath));
        } catch (IOException e) {
            throw new RuntimeException("Error loading image bytes: " + e.getMessage());
        }
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
        return avatarBytes != null ? Arrays.hashCode(avatarBytes) : 0;
    }
}
