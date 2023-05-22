package Entity;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Message implements Serializable {
    private final User sender;
    private final List<User> recipients;
    private final String text;
    private final LocalDateTime serverReceiveTime;
    private LocalDateTime deliveryTime;
    private final HashSet<String> recipientStrings;
    private final String imagePath;

    public Message(User sender, List<User> recipients, String text, ImageIcon imageFile) {
        this.sender = sender;
        this.recipients = recipients;
        this.recipientStrings = new HashSet<>(recipients.size());
        recipientStrings.addAll(recipients.stream().map(User::getUsername).toList());

        if (imageFile != null) {
            this.imagePath = "data\\temp\\" + imageFile.hashCode();
            if (!Files.exists(Paths.get(this.imagePath))){
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.imagePath));
                    oos.writeObject(imageFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            this.imagePath = null;
        }

        this.text = text;
        this.serverReceiveTime = LocalDateTime.now();
    }

    public User getSender() {
        return sender;
    }

    public List<User> getRecipients() {
        return recipients;
    }

    public String getText() {
        return text;
    }

    public ImageIcon getImageFile() {
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

    public LocalDateTime getServerReceiveTime() {
        return serverReceiveTime;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Set<String> getRecipientStrings() {
        return recipientStrings;
    }
}
