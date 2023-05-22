package GUI;

import Entity.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class UserList extends JList<User> implements Scrollable {
    private final UserListModel model = new UserListModel();

    public UserList() {
        super();
        setCellRenderer(new onlineUsersRenderer());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLoweredBevelBorder());
        setModel(model);
    }

    public void add(User user) {
        model.addElement(user);
    }

    public void addAll(Collection<User> user) {
        model.addAll(user);
    }

    public void remove(User user) {
        model.removeElement(user);
    }

    public void removeAllMatches(Collection<User> users) {
        for (User user : users) {
            if (this.contains(user)) {
                model.removeElement(user);
            }
        }
    }

    public void clear() {
        model.clear();
    }

    public boolean contains(User user) {
        return model.contains(user);
    }

    @Override
    public ArrayList<User> getSelectedValuesList() {
        UserListModel dm = (UserListModel) getModel();
        int[] selectedIndices = getSelectedIndices();

        if (selectedIndices.length > 0) {
            int size = dm.getSize();
            if (selectedIndices[0] >= size) {
                return new ArrayList<>();
            }
            ArrayList<User> selectedItems = new ArrayList<User>();
            for (int i : selectedIndices) {
                if (i >= size) {
                    break;
                } else if (dm.getElementAt(i) == null) {
                    continue;
                }

                selectedItems.add(dm.getElementAt(i));
            }
            return selectedItems;
        }
        return new ArrayList<>();
    }

    @Override
    public User getSelectedValue() {
        return getSelectedIndex() == -1 ? null : getModel().getElementAt(getSelectedIndex());
    }

    public void removeIf(Predicate<? super User> filter) {
        model.removeIf(filter);
    }

    public ArrayList<User> getUsers() {
        return model.getUsers();
    }
}
