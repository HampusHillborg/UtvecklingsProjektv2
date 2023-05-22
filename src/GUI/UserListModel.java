package GUI;

import Entity.User;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class UserListModel implements ListModel<User>{
    private final ArrayList<User> internalList;
    private final ArrayList<ListDataListener> listeners = new ArrayList<>();
    private final User placeholder;

    public UserListModel() {
        super();
        internalList = new ArrayList<User>();
        // Create a transparent 256x256 image for the placeholder

        placeholder = new User("None", new ImageIcon(new ImageIcon("data\\GUI\\placeholder.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
        internalList.add(placeholder);
    }

    @Override
    public int getSize() {
        return internalList.size();
    }

    @Override
    public User getElementAt(int index) {
        return internalList.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    public boolean contains(User user) {
        return internalList.stream().anyMatch(u -> u.getUsername().equals(user.getUsername()));
    }

    public void addElement(User user) {
        if (user == placeholder) return; // Don't add the placeholder

        internalList.add(user);
        for (ListDataListener listener : listeners) {
            listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, internalList.size()-1, internalList.size()));
        }
    }

    public void removeElement(User user) {
        if (user == placeholder) return; // Don't remove the placeholder

        internalList.remove(user);
        for (ListDataListener listener : listeners) {
            listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, internalList.size()));
        }
    }

    public void removeIf(Predicate<? super User> filter) {
        // Add && user != placeholder to the filter to prevent removing the placeholder
        final int[] minIndex = {0};
        final int[] maxIndex = {internalList.size() - 1};

        filter = filter.and(user -> user != placeholder);
        internalList.stream().filter(filter).forEach(user -> {
            int userIndex = internalList.indexOf(user);
            minIndex[0] = (minIndex[0] > internalList.indexOf(user)) ? userIndex : minIndex[0];
            maxIndex[0] = (maxIndex[0] < internalList.indexOf(user)) ? userIndex : maxIndex[0];
            internalList.remove(user);
        });

        for (ListDataListener listener : listeners) {
            listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, minIndex[0], maxIndex[0]));
        }
    }

    public void addAll(Collection<User> user) {
        user.remove(placeholder);

        internalList.addAll(user);
        for (ListDataListener listener : listeners) {
            listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, internalList.size(), internalList.size() + user.size() - 1));
        }
    }

    public void clear() {
        internalList.clear();
        internalList.add(placeholder);
        for (ListDataListener listener : listeners) {
            listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, internalList.size()));
        }
    }

    public ArrayList<User> getUsers() {
        return internalList.stream().filter(user -> user != placeholder).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
