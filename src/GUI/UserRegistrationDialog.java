package GUI;

import Entity.User;
import Sockets.Client;

import javax.swing.*;
import java.awt.*;

public class UserPanel extends JPanel {
    private Client client;
    private User user;
    private JLabel userCount;
    public UserPanel(User user, Client client) {
        super();
        this.client = client;
        this.user = user;
        setLayout(new GridLayout(2,3));

        JPanel username_labels = new JPanel();
        JLabel username_label = new JLabel("Username: ");
        username_label.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel username = new JLabel(user.getUsername());
        username.setFont(new Font("Arial", Font.PLAIN, 20));

        userCount = new JLabel("Active users: 0");
        userCount.setFont(new Font("Arial", Font.PLAIN, 20));

        username_labels.add(username_label);
        username_labels.add(username);
        add(username_labels);
        add(userCount);
    }

    public void setUserCount(int newValue) {
        userCount.setText("Active users: " + newValue);
    }

}
