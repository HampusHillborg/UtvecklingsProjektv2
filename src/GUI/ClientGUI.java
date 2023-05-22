package GUI;

import Entity.Message;
import Entity.User;
import Sockets.Client;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLWriter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.naming.Context;

public class ClientGUI extends JFrame implements PropertyChangeListener {
    private final MainFrame mf;
    private final UserPanel up;
    private final JScrollPane usersScrollPane;
    private final JScrollPane chatScrollPane;
    private int port;
    private String host;
    // private JTextPane chatLog;
    private final ChatPanel chatPanel;
    private User user;
    private final Client client;
    private final JFrame frame;
    private boolean loggedIn = false;
    private final UserList onlineUsersList;
    private UserList receiverUserList;
    private final UserList contactUserList;
    private final ArrayList<UserList> userLists;
    private final Container usersScrollContainer;
    private ImageIcon messageIcon;

    public ClientGUI(Client client, boolean showGUI) {
        super();
        this.client = client;
        client.addPropertyChangeListener(this);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        frame = new JFrame("ClientGUI");
        mf = new MainFrame(client, screenSize.width/2, screenSize.height/2);


        // Add chat log
        /* chatLog = new JTextPane();
        JScrollPane chatScrollPane = new JScrollPane(chatLog);
        chatLog.setEditable(false); */
        chatPanel = new ChatPanel();
        chatScrollPane = new JScrollPane(chatPanel);

        // Add message input box
        JTextArea messageField = new JTextArea();
        messageField.setColumns(40);
        messageField.setLineWrap(true); // wrap text to next line when it reaches the edge of the text area
        messageField.setWrapStyleWord(true); // wrap text at word boundaries

        JButton sendButton = new JButton("Send");
        JLabel messageIconLabel = new JLabel();
        JButton imageUpload = new JButton("Upload Image");
        JPanel messagePanel = new JPanel();


        sendButton.addActionListener(e -> {
            // code to execute when button is clicked
            // appendToChatHistory(user.getAvatar(), user.getUsername() + ": " + messageField.getText() + "\n", null);
            Message message = new Message(user, receiverUserList.getUsers(), messageField.getText(), messageIcon);
            client.sendMessage(message);

            messageIcon = null;
            messageIconLabel.setIcon(null);
            messageField.setText("");
        });

        imageUpload.addActionListener(e -> {
            // Create a file chooser
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));

            // Show the file chooser dialog
            int result = chooser.showOpenDialog(ClientGUI.this);

            // If the user selected a file, upload it
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                // Create an ImageIcon from the selected file
                ImageIcon icon = new ImageIcon(file.getAbsolutePath());

                // Resize the image to a maximum width of 50 pixels and a maximum height of 50 pixels
                /*int width = icon.getIconWidth();
                int height = icon.getIconHeight();
                int max = Math.max(width, height);
                if (max > 50) {
                    width = (int) (width * 50.0 / max);
                    height = (int) (height * 50.0 / max);
                }
                icon.setImage(icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));*/

                // Append a message with the image to the chat history
                // appendToChatHistory(user.getAvatar(),user.getUsername() + ": ", icon);
                messageIcon = new ImageIcon(icon.getImage());

                // Resize messageIcon to a maximum width of 256 pixels and a maximum height of 256 pixels
                int width = icon.getIconWidth();
                int height = icon.getIconHeight();
                int max = Math.max(width, height);
                if (max > 256) {
                    width = (int) (width * 256.0 / max);
                    height = (int) (height * 256.0 / max);
                }
                messageIcon.setImage(messageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));

                // Resize the image to a maximum width of 32 pixels and a maximum height of 32 pixels
                width = icon.getIconWidth();
                height = icon.getIconHeight();
                max = Math.max(width, height);
                if (max > 32) {
                    width = (int) (width * 64.0 / max);
                    height = (int) (height * 64.0 / max);
                }
                icon.setImage(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
                messageIconLabel.setIcon(icon);
            }
        });

        // Add online users box
        onlineUsersList = new UserList();
        onlineUsersList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        onlineUsersList.setCellRenderer(new onlineUsersRenderer());
        onlineUsersList.addPropertyChangeListener(this);

        // Add receiver users box
        receiverUserList = new UserList();
        onlineUsersList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        receiverUserList.setCellRenderer(new onlineUsersRenderer());
        // Add contact users box
        contactUserList = new UserList();
        contactUserList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        contactUserList.setCellRenderer(new onlineUsersRenderer());

        // Add user lists to arraylist
        userLists = new ArrayList<>();
        userLists.add(onlineUsersList);
        userLists.add(receiverUserList);
        userLists.add(contactUserList);

        JButton addToContactList = new JButton("Add to contactlist");
        JButton addToReceiverList = new JButton("Add to receiverslist");

        usersScrollContainer = new Container();
        usersScrollContainer.setBackground(Color.WHITE);
        usersScrollContainer.setLayout(new BoxLayout(usersScrollContainer, BoxLayout.Y_AXIS));
        addToContactList.addActionListener(e -> {
            // code to execute when button is clicked
            // appendToChatHistory(user.getAvatar(), user.getUsername() + ": " + messageField.getText() + "\n", null);

            ArrayList<User> users = onlineUsersList.getSelectedValuesList();
            users.removeAll(receiverUserList.getSelectedValuesList());
            users.addAll(receiverUserList.getSelectedValuesList());
            users.stream().filter(user -> !contactUserList.contains(user)).forEach(contactUserList::add);
            contactUserList.removeAllMatches(contactUserList.getSelectedValuesList());

            onlineUsersList.setSelectedIndex(0);
            contactUserList.setSelectedIndex(0);
            receiverUserList.setSelectedIndex(0);
            usersScrollContainer.revalidate();
            usersScrollContainer.repaint();
        });

        addToReceiverList.addActionListener(e -> {
            // code to execute when button is clicked
            // appendToChatHistory(user.getAvatar(), user.getUsername() + ": " + messageField.getText() + "\n", null);;

            ArrayList<User> users = onlineUsersList.getSelectedValuesList();
            users.removeAll(contactUserList.getSelectedValuesList());
            users.addAll(contactUserList.getSelectedValuesList());
            users.stream().filter(user -> !receiverUserList.contains(user)).forEach(receiverUserList::add);
            receiverUserList.removeAllMatches(receiverUserList.getSelectedValuesList());

            onlineUsersList.setSelectedIndex(0);
            contactUserList.setSelectedIndex(0);
            receiverUserList.setSelectedIndex(0);
            usersScrollContainer.revalidate();
            usersScrollContainer.repaint();
        });

        JLabel onlineUserLabel = new JLabel("Online users:");
        usersScrollContainer.add(onlineUserLabel);
        usersScrollContainer.add(onlineUsersList);
        JLabel receiverUserLabel = new JLabel("Receivers:");
        usersScrollContainer.add(receiverUserLabel);
        usersScrollContainer.add(receiverUserList);
        JLabel contactUserLabel = new JLabel("Contacts:");
        usersScrollContainer.add(contactUserLabel);
        usersScrollContainer.add(contactUserList);

        usersScrollPane = new JScrollPane(usersScrollContainer);
        usersScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        usersScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        usersScrollPane.setPreferredSize(new Dimension(200, 500));


        if (showGUI) {
            UserRegistrationDialog reg_dialog = new UserRegistrationDialog();

            // Wait for first login
            while (!reg_dialog.finished()) {
            }
            user = reg_dialog.getUser();
            host = reg_dialog.getHost();
            port = reg_dialog.getPort();
        }
        else {

            host = "localhost";
            port = 12345;
            String username = Arrays.toString(new Random().ints(0, 10).limit(30).collect(ArrayList::new, ArrayList::add, ArrayList::addAll).toArray(Object[]::new));
            // Hash the username for a new username, limit the output to 6 characters
            username = String.format("%s", username.getBytes(StandardCharsets.UTF_8));
            // get all image files from the data\\AutoGeneratedAvatarsResized folder
            File folder = new File("data\\AutoGeneratedAvatarsResized");
            File[] listOfFiles = folder.listFiles();
            assert listOfFiles != null;
            String avatar = listOfFiles[new Random().nextInt(listOfFiles.length)].getName();
            if (avatar.contains("."))
                avatar = avatar.substring(0, avatar.lastIndexOf("."));

            avatar = avatar + ".jpg";
            ImageIcon avatarIcon = new ImageIcon("data\\AutoGeneratedAvatarsResized\\" + avatar);
            user = new User(username, avatarIcon);
        }

        loggedIn = true;
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        chatScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        messagePanel.add(messageIconLabel);
        messagePanel.add(imageUpload);
        messagePanel.add(messageField);
        messagePanel.add(sendButton);
        messagePanel.add(addToReceiverList);
        messagePanel.add(addToContactList);
        up = new UserPanel(user, client);
        mf.add(up, BorderLayout.NORTH, 0);
        mf.add(chatScrollPane, BorderLayout.CENTER, 1);
        mf.add(messagePanel, BorderLayout.SOUTH, 2);
        mf.add(usersScrollPane, BorderLayout.EAST, 3);
        // mf.add(receiversScrollPane, BorderLayout.EAST, 4);
        frame.add(mf);
        frame.setLocation(screenSize.width/4, screenSize.height/4);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(showGUI);
    }



    @Override
    @SuppressWarnings("unchecked")
    public void propertyChange(PropertyChangeEvent evt) {
        // Whenever a login happens, update the user, host and port
        if (evt.getPropertyName().equals("login")){
            user = (User) evt.getNewValue();
            host = (String) evt.getNewValue();
            port = (int) evt.getNewValue();
            loggedIn = true;
        }
        else if (evt.getPropertyName().equals("onlineUsers")){
            ArrayList<User> users = (ArrayList<User>) evt.getNewValue();
            User[] userArray = users.toArray(new User[0]);
            for (User user : userArray) {
                if (!onlineUsersList.contains(user)) {
                    onlineUsersList.add(user);
                }
            }

            onlineUsersList.removeIf(user -> !users.contains(user));
            for (UserList list : userLists) {
                list.revalidate();
                list.repaint();
            }

            usersScrollContainer.revalidate();
            usersScrollContainer.repaint();

            usersScrollPane.revalidate();
            usersScrollPane.repaint();
        } else if (evt.getPropertyName().equals("userCount")) {
            int userCount = (int) evt.getNewValue();
            up.setUserCount(userCount);
        } else if (evt.getPropertyName().equals("message")) {
            Message message = (Message) evt.getNewValue();
            appendToChatHistory(message);
            chatScrollPane.revalidate();
            chatScrollPane.getVerticalScrollBar().setValue(usersScrollPane.getVerticalScrollBar().getMaximum());
            chatScrollPane.repaint();
        } else if (evt.getPropertyName().equals("avatarLoaded")) {
            onlineUsersList.revalidate();
            onlineUsersList.repaint();
            usersScrollPane.revalidate();
            usersScrollPane.repaint();
        }

        mf.repaint();
        frame.repaint();
    }

    /* private void appendToChatHistory(ImageIcon userAvatar, String message, ImageIcon imageIcon) {
        // Create a new text pane for the message
        JTextPane messagePane = new JTextPane();
        messagePane.setEditable(false);
        messagePane.setOpaque(false);

        if (userAvatar != null){
            // Resize the image to a maximum width of 50 pixels and a maximum height of 50 pixels
            int width = userAvatar.getIconWidth();
            int height = userAvatar.getIconHeight();
            int max = Math.max(width, height);
            if (max > 64) {
                width = (int) (width * 64.0 / max);
                height = (int) (height * 64.0 / max);
            }
            userAvatar.setImage(userAvatar.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));

            // Create a new style for the icon
            Style avatarStyle = messagePane.addStyle("avatarStyle", null);
            StyleConstants.setIcon(avatarStyle, userAvatar);


            // Insert the icon into the document at the beginning
            StyledDocument doc = messagePane.getStyledDocument();
            try {
                doc.insertString(0, "ignored text", avatarStyle);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }

        }
        // Set the message text
        Style textStyle = messagePane.addStyle("textStyle", null);
        messagePane.setText(message);

        // Set the message icon if it exists
        if (imageIcon != null) {
            // Resize the image to a maximum width of 50 pixels and a maximum height of 50 pixels
            int width = imageIcon.getIconWidth();
            int height = imageIcon.getIconHeight();
            int max = Math.max(width, height);
            if (max > 128) {
                width = (int) (width * 128.0 / max);
                height = (int) (height * 128.0 / max);
            }
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));

            // Create a new style for the icon
            Style iconStyle = messagePane.addStyle("iconStyle", null);
            StyleConstants.setIcon(iconStyle, imageIcon);


            // Insert the icon into the document at the beginning
            StyledDocument doc = messagePane.getStyledDocument();
            try {
                doc.insertString(0, "ignored text", iconStyle);
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }

        // Add the message pane to the chat history
        StyledDocument chatDoc = chatLog.getStyledDocument();
        try {
            chatDoc.insertString(chatDoc.getLength(), " \n", null);
            chatDoc.insertString(chatDoc.getLength(), " ", messagePane.getStyle("avatarStyle"));
            chatDoc.insertString(chatDoc.getLength(), message, messagePane.getStyle("textStyle"));
            // Vertically center next string
            chatDoc.insertString(chatDoc.getLength(), " ", messagePane.getStyle("iconStyle"));

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        // Scroll to the bottom of the chat history
        chatLog.setCaretPosition(chatLog.getDocument().getLength());
    } */

    public void appendToChatHistory(Message message){
        chatPanel.appendToChatHistory(message);
        chatPanel.revalidate();
        chatPanel.repaint();
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public User getUser() {
        return user;
    }

    public boolean loggedIn(){
        return loggedIn;
    }

    private class MainFrame extends JPanel {
        public MainFrame(Client client, int x, int y) {
            super();
            setFocusable(true);
            setPreferredSize(new Dimension(x, y));
            BorderLayout contraints = new BorderLayout();
            setLayout(contraints);
        }
    }
}