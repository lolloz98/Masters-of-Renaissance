package it.polimi.ingsw.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private static final Logger logger = LogManager.getLogger(Server.class);

    // fixme: Don't use fixed port number
    public final static int PORT = 16509;

    public static void main(String[] args)
    {
        ServerSocket socket;
        try {
            socket = new ServerSocket(PORT);
            logger.info("Server listening on port: " + PORT);
        } catch (IOException e) {
            logger.error("Cannot find a port: " + e.getMessage());
            System.exit(1);
            return;
        }

        while (true) {
            try {
                Socket client = socket.accept();
                ClientHandler clientHandler = new ClientHandler(client);
                Thread thread = new Thread(clientHandler, "server_" + client.getInetAddress());
                thread.start();
            } catch (IOException e) {
                logger.error("Connection dropped: " + e.getMessage());
            }
        }
    }
}
