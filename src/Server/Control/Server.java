package Server.Control;

import Client.Control.Client;
import Server.Entity.Message;
import Server.Entity.User;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server extends Thread {
    private int port;
    private HashMap<User, Client> activeUsers;

    public Server(int port) {
        this.port = port;
        activeUsers = new HashMap<>();
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
        private Socket socket;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;
        private User user;

        public ClientHandler(Socket socket, int clientCount) {
            this.socket = socket;
        }

        public void run() {
            try {
                ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));

                // Första objektet som servern får från Client är alltid ett user objekt.
                user = (User) ois.readObject();
                System.out.println(user.getUsername() + " anslöt sig.");
                clientConnected(this);

                // Sedan kan klienten bara skicka Message-objekt.
                while (true) {
                    Object obj = ois.readObject();

                    if (obj instanceof Message) {
                        Message msg = (Message) obj;
                        if (msg.getText().equals("//disconnect")) {
                            clientDisconnected(user);
                            System.out.println("En klient disconnetar!");
                        } else {
                            sendMessage(msg);
                        }
                    }

                    if (obj instanceof User) {
                        // Jämför namnet med aktiva klienter. Där det matchar uppdatera den klientens kontakter.
                        User user = (User) obj;
                        updateActiveClientUser(user, this);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                clientDisconnected(user);
            }
        }
    }

    public void sendMessage(Message message) {
        // Implementation för att skicka meddelanden till klienter
    }

    public void updateActiveClientUser(User user, ClientHandler clientHandler) {
        // Implementation för att uppdatera klientens användarinformation och kontakter
    }

    public void clientConnected(ClientHandler clientHandler) {
        // Implementation för att hantera händelse när en klient ansluter
    }

    public void clientDisconnected(User user) {
        // Implementation för att hantera händelse när en klient kopplar från
    }

    public static void main(String[] args) {
        Server server = new Server(12345);
        server.start();
    }
}
