package GUI;

import Entity.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class OnlineUsersRenderer extends JLabel implements ListCellRenderer<User> {
    private ImageIcon placeholder;
    private boolean avatarLoaded;
    static HashMap<Integer, ImageIcon> avatarCache = new HashMap<>();

    public OnlineUsersRenderer() {
        super();
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends User> list, User value, int index, boolean isSelected, boolean cellHasFocus) {
        setOpaque(true);

        if (!avatarCache.containsKey(value.getAvatarHash())){
            Image image = value.getAvatar().getImage();
            image = image.getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH);
            avatarCache.put(value.getAvatarHash(), new ImageIcon(image));
        }

        setText(value.getUsername());
        setIcon(avatarCache.get(value.getAvatarHash()));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 32));
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        return this;
    }
}
