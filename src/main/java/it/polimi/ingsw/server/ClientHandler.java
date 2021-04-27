package it.polimi.ingsw.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private static final Logger logger = LogManager.getLogger(ClientHandler.class);

    private final Socket client;
    private ObjectOutputStream oStream;
    private ObjectInputStream iStream;

    public ClientHandler(Socket client){
        this.client = client;
        // todo: add reference to controller/view
    }

    private void handleClientConnection() throws IOException
    {
        try {
            while (true) {
                Object next = iStream.readObject();
                logger.debug("input from client: " + next);
                oStream.writeObject(next);
            }
        } catch (ClassNotFoundException | ClassCastException e) {
            System.out.println("invalid stream from client");
        }
    }

    @Override
    public void run() {
        try{
            oStream = new ObjectOutputStream(client.getOutputStream());
            iStream = new ObjectInputStream(client.getInputStream());
        } catch (IOException e){
            logger.error("Can't open the connection to " + client.getInetAddress());
            closeConnection();
            return;
        }

        logger.info("Connected to " + client.getInetAddress());

        try {
            handleClientConnection();
        } catch (IOException e) {
            logger.warn("client " + client.getInetAddress() + " connection dropped");
        }

        closeConnection();
    }

    public void closeConnection(){
        try{
            client.close();
        }catch(IOException e){
            logger.warn("Error happened while closing a connection: " + e.getMessage());
        }
    }
}
