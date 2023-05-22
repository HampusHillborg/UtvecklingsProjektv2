package GUI;

import Entity.Message;

import javax.swing.*;
import java.awt.*;

public class ChatPanel extends JPanel {
    private final GroupLayout layout;
    private final GroupLayout.ParallelGroup h_group;
    private final GroupLayout.SequentialGroup v_group;

    public ChatPanel() {
        super();
        // Set margins
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // Set layout
        layout = new GroupLayout(this);
        v_group = layout.createSequentialGroup();
        h_group = layout.createParallelGroup();

        layout.setHorizontalGroup(h_group);
        layout.setVerticalGroup(v_group);
        setLayout(layout);
        setBackground(Color.WHITE);
    }
    public void appendToChatHistory(Message message) {
        JPanel messagePanel = new JPanel();
        GroupLayout messageLayout = new GroupLayout(messagePanel);

        ImageIcon avatar = message.getSender().getAvatar();
        // Resize the image to size 64x64
        int width = avatar.getIconWidth();
        int height = avatar.getIconHeight();
        int max = Math.max(width, height);
        if (max > 64) {
            width = (int) (width * 64.0 / max);
            height = (int) (height * 64.0 / max);
        }

        avatar = new ImageIcon(avatar.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        JLabel avatarLabel = new JLabel(avatar);
        avatarLabel.setText(message.getSender().getUsername() + ": ");
        avatarLabel.setFont(new Font("Arial", Font.BOLD, 18));
        avatarLabel.setVerticalTextPosition(JLabel.CENTER);
        avatarLabel.setHorizontalAlignment(SwingConstants.LEFT);
        avatarLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel messageContent = new JPanel();
        JTextArea messageLabel = new JTextArea(message.getText());
        messageContent.add(messageLabel);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        messageLabel.setBackground(messageContent.getBackground());
        messageLabel.setAlignmentY(messageContent.getHeight()/2.0f);

        if (message.getImageFile() != null) {
            ImageIcon message_icon = message.getImageFile();
            // Resize the image to size 64x64
            width = message_icon.getIconWidth();
            height = message_icon.getIconHeight();
            max = Math.max(width, height);
            if (max > 128) {
                width = (int) (width * 256.0 / max);
                height = (int) (height * 256.0 / max);
            }

            messageContent.add(new JLabel(new ImageIcon(message_icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH))));
        }


        messageLayout.setHorizontalGroup(
                messageLayout.createSequentialGroup()
                        .addComponent(avatarLabel)
                        .addGap(10)
                        .addComponent(messageContent)
        );

        messagePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        messagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Set the maximum height of the message panel to fit its content only
        messagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, messagePanel.getPreferredSize().height));
        messagePanel.setBorder(BorderFactory.createLoweredBevelBorder());
        add(messagePanel);

        v_group.addComponent(messagePanel).addGap(5);
        h_group.addComponent(messagePanel);
        // Trigger the layout manager to layout the components and repaint the panel
        revalidate();
        repaint();
    }
}
