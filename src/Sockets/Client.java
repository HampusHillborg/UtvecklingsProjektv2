package Sockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    private String serverAddress;
    private int serverPort;

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void start() {
        try {
            Socket socket = new Socket(serverAddress, serverPort);
            System.out.println("Connected to server: " + serverAddress + ":" + serverPort);

            // Perform client operations
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            // Send/receive data through inputStream/outputStream

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

        Client client = new Client(serverAddress, serverPort);
        client.start();
    }
}
