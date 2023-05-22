package Client.Control;

import Server.Entity.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private String serverAddress;
    private int serverPort;
    private String username;
    private String imagePath;

    public Client(String serverAddress, int serverPort, String username, String imagePath) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.username = username;
        this.imagePath = imagePath;
    }

    public void start() {
        try {
            Socket socket = new Socket(serverAddress, serverPort);
            System.out.println("Connected to server: " + serverAddress + ":" + serverPort);

            // Perform client operations
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            // Send user information (username and image) to the server
            User user = new User(username, imagePath);
            outputStream.writeObject(user);
            outputStream.flush();

            // Perform other operations like sending/receiving messages, displaying connected users, etc.

            // Close the socket after finishing communication
            socket.close();
            System.out.println("Disconnected from server");
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String serverAddress = "localhost"; // Replace with the server IP address
        int serverPort = 12345; // Replace with the server port
        String username = "JohnDoe"; // Replace with the desired username
        String imagePath = "path/to/image.jpg"; // Replace with the path to the user's image

        Client client = new Client(serverAddress, serverPort, username, imagePath);
        client.start();
    }
}
