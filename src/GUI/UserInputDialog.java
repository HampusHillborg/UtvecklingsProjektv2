package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import Entity.User;
import Sockets.Server;


public class UserInputDialog extends JDialog implements ActionListener {
    private JTextField usernameField;
    private JLabel imageLabel;
    private JButton chooseImageButton;
    private JButton okButton;
    private File selectedFile;
    private Server server;

    public UserInputDialog(JFrame parent) {
        super(parent, "New Entity.User", true);
        setSize(400, 200);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));

        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Image:"));
        imageLabel = new JLabel();
        inputPanel.add(imageLabel);

        add(inputPanel, BorderLayout.CENTER);

        chooseImageButton = new JButton("Choose Image");
        chooseImageButton.addActionListener(this);
        add(chooseImageButton, BorderLayout.SOUTH);

        okButton = new JButton("OK");
        okButton.addActionListener(this);
        add(okButton, BorderLayout.EAST);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == chooseImageButton) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                imageLabel.setIcon(new ImageIcon(selectedFile.getAbsolutePath()));
            }
        } else if (e.getSource() == okButton) {
            String username = usernameField.getText();
            if (selectedFile != null && !username.isEmpty()) {
                // Create new Entity.User object with username and selected image
                User newUser = new User(username, new ImageIcon(selectedFile.getAbsolutePath()));

                // Add the new user to connectedUsers list
                server.getConnectedUsers().add(newUser);

                // Close the dialog
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a username and choose an image.");
            }
        }
    }
}