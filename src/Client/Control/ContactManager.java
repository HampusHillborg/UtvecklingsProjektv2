package Client.Control;

import Client.Entity.Contact;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContactManager {
    private List<Contact> contacts;
    private String contactsFilePath;

    public ContactManager(String contactsFilePath) {
        this.contacts = new ArrayList<>();
        this.contactsFilePath = contactsFilePath;
        loadContacts();
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public void removeContact(Contact contact) {
        contacts.remove(contact);
    }

    public void saveContacts() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(contactsFilePath))) {
            outputStream.writeObject(contacts);
        } catch (IOException e) {
            System.out.println("Error saving contacts: " + e.getMessage());
        }
    }

    private void loadContacts() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(contactsFilePath))) {
            Object object = inputStream.readObject();
            if (object instanceof List) {
                contacts = (List<Contact>) object;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading contacts: " + e.getMessage());
        }
    }
}
