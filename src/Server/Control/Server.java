package Server.Control;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void run() {
        System.out.println("Server started");
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            int count = 0;
            while (true) {
                Socket clientSocket = serverSocket.accept();
                count++;
                System.out.println("Client connected: " + count);
                ClientHandler clientHandler = new ClientHandler(clientSocket, count);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println("Error starting the server: " + e.getMessage());
        }
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private int clientNumber;

        public ClientHandler(Socket clientSocket, int clientNumber) {
            this.clientSocket = clientSocket;
            this.clientNumber = clientNumber;
        }

        public void run() {
            // Perform client-specific operations
            // e.g., read/write to the clientSocket
            try {
                // Handle client requests here
            } finally {
                try {
                    clientSocket.close();
                    System.out.println("Client disconnected: " + clientNumber);
                } catch (IOException e) {
                    System.out.println("Error closing client " + clientNumber + " socket: " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server(12345);
        server.start();
    }
}

